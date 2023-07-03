package moe.tabidachi.sim.ui.feature.home

import android.app.Application
import android.content.Context
import android.net.Uri
import android.telephony.TelephonyManager
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.getSystemService
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import moe.tabidachi.sim.ShellUtil
import moe.tabidachi.sim.data.database.Database
import moe.tabidachi.sim.data.database.entity.Country
import moe.tabidachi.sim.data.database.entity.SimOperator
import moe.tabidachi.sim.ktx.toast
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val context: Application,
    private val database: Database,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _stateFlow: MutableStateFlow<HomeState> = MutableStateFlow(HomeState())

    val stateFlow: StateFlow<HomeState> = _stateFlow.asStateFlow()

    init {
        viewModelScope.launch {
            database.countryDao().getAllFlow().collect { operators ->
                _stateFlow.update {
                    it.copy(favoriteCountry = operators)
                }
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            val telephonyManager = context.getSystemService<TelephonyManager>()
            telephonyManager?.let {
                database.countryDao()
                    .queryByCountryCode(telephonyManager.simCountryIso.uppercase())
                    ?.let { country ->
                        getSystemOperator(telephonyManager, country)
                    } ?: run {
                    database.operatorDao().queryByCountryCode(
                        telephonyManager.simCountryIso.uppercase()
                    ).asSequence().filter {
                        !it.countryName.isNullOrBlank() && !it.countryCode.isNullOrBlank()
                    }.map {
                        Country(it.countryName!!, it.countryCode!!)
                    }.firstOrNull()?.let { country ->
                        database.countryDao().add(country)
                        getSystemOperator(telephonyManager, country)
                    }
                }
            }
        }
    }

    private suspend fun getSystemOperator(telephonyManager: TelephonyManager, country: Country) {
        setCountry(country)
        queryOperator(country)
        _stateFlow.value.operators.firstOrNull {
            "${it.mcc}${it.mnc}" == telephonyManager.simOperator
        }?.let(::setOperator)
    }

    fun countryExpandedChange(expanded: Boolean) {
        _stateFlow.update {
            it.copy(countryExpanded = expanded)
        }
    }

    fun operatorExpandedChange(expanded: Boolean) {
        _stateFlow.update {
            it.copy(operatorExpanded = expanded)
        }
    }

    fun dialogVisibleChange(visible: Boolean) {
        _stateFlow.update {
            it.copy(dialogVisible = visible)
        }
    }

    fun changeCountry(country: Country) {
        setCountry(country)
        viewModelScope.launch {
            queryOperator(country)
        }
    }

    private fun setCountry(country: Country) {
        _stateFlow.update {
            it.copy(country = country)
        }
    }

    private suspend fun queryOperator(country: Country) = withContext(Dispatchers.IO) {
        val operators = database.operatorDao().queryByCountryCode(country.countryCode)
        _stateFlow.update {
            it.copy(operators = operators, operator = operators.firstOrNull())
        }
    }

    fun setOperator(operator: SimOperator) {
        _stateFlow.update {
            it.copy(operator = operator)
        }
    }

    fun done() {
        val operator = _stateFlow.value.operator ?: run {
            context.toast("请选择一个运营商")
            return
        }
        ShellUtil.enableLocationReport("${operator.mcc}${operator.mnc}", operator.countryCode!!)
            .onSuccess {
                context.toast("应用成功")
            }.onFailure {
                context.toast("无法获取 root 权限")
            }
    }

    fun menuExpandedChange(expanded: Boolean) {
        _stateFlow.update {
            it.copy(menuExpanded = expanded)
        }
    }

    fun aboutDialogVisibleChange(visible: Boolean) {
        _stateFlow.update {
            it.copy(aboutDialogVisible = visible)
        }
    }

    fun aboutClick(context: Context) {
        val uri = Uri.parse("https://github.com/tabidachinokaze/LocationReportEnabler")
        val intent = CustomTabsIntent.Builder()
            .build()
        intent.launchUrl(context, uri)
    }
}