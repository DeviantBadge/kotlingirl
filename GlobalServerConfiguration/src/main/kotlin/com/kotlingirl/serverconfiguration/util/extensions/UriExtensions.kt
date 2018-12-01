package com.kotlingirl.serverconfiguration.util.extensions

// i skip errors in uri, so we parse ..?A=B&V&C=D into (A,B); (C,D)
// does not parse correctly arrays in arguments
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