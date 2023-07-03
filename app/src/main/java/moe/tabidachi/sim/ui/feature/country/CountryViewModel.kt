package moe.tabidachi.sim.ui.feature.country

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import moe.tabidachi.sim.data.database.Database
import moe.tabidachi.sim.data.database.entity.Country
import moe.tabidachi.sim.data.database.entity.SimOperator
import moe.tabidachi.sim.ktx.regex
import javax.inject.Inject

@OptIn(ExperimentalSerializationApi::class, FlowPreview::class)
@HiltViewModel
class CountryViewModel @Inject constructor(
    @ApplicationContext
    context: Context,
    private val database: Database,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _stateFlow: MutableStateFlow<CountryState> = MutableStateFlow(CountryState())

    val stateFlow: StateFlow<CountryState> = _stateFlow.asStateFlow()

    private val operators: List<SimOperator>

    private val filter = MutableStateFlow("")
    private val countries: Flow<List<Country>> = database.countryDao().getAllFlow()

    init {
        viewModelScope.launch {
            filter.debounce(500).collectLatest {
                filter(it)
            }
        }
        viewModelScope.launch {
            countries.collect() { countries ->
                _stateFlow.update {
                    it.copy(country = countries)
                }
            }
        }
        database.countryDao()
        operators = context.assets.open("SimOperators.json").use {
            Json.decodeFromStream<List<SimOperator>>(it)
        }
        viewModelScope.launch {
            filter("")
        }
    }

    fun onFilterChange(value: String) {
        filter.value = value
        _stateFlow.update {
            it.copy(filter = value, isTextEmpty = value.isEmpty())
        }
    }

    fun onFilterClear() {
        onFilterChange("")
    }

    private fun filter(value: String) {
        val value = value.lowercase()
        operators.asSequence().filter {
            !it.countryName.isNullOrBlank() && !it.countryCode.isNullOrBlank()
        }.filter {
            value.regex().matches(it.countryName?.lowercase() ?: "") || value.regex().matches(it.countryCode?.lowercase() ?: "")
        }.map { operator ->
            CountryItem(operator.countryName!!, operator.countryCode!!)
        }.distinct().sortedByDescending {  item ->
            _stateFlow.value.country.any {  it.countryName == item.countryName && it.countryCode == item.countryCode  }
        }.toList().let { countries ->
            _stateFlow.update {
                it.copy(countries = countries)
            }
        }
    }

    fun addCountry(countryItem: CountryItem) = viewModelScope.launch(Dispatchers.IO) {
        database.countryDao().add(Country(countryItem.countryName, countryItem.countryCode))
    }

    fun removeCountry(countryItem: CountryItem) = viewModelScope.launch(Dispatchers.IO) {
        database.countryDao().delete(Country(countryItem.countryName, countryItem.countryCode))
    }
}