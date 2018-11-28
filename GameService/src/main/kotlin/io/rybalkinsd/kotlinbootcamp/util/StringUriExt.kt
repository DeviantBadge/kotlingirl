package io.rybalkinsd.kotlinbootcamp.util

fun String.queryToMap(): Map<String, String> =
        split("&")
            .map { it.split("=") }
            .filter { it.size == 2 }
            .associate { list -> list[0] to list[1] }


fun String.getUriQuery(): Map<String, String> {
    val delimiter = indexOfFirst { it == '?' }
    if (delimiter < 0)
        return mapOf()
    return substring(delimiter).queryToMap()
}