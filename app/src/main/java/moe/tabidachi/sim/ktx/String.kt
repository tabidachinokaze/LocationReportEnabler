package moe.tabidachi.sim.ktx

fun String.regex(): Regex {
    return this.toCharArray().joinToString("", "^", ".+") {
        "(?=.*$it)"
    }.toRegex()
}