package com.kotlingirl.serverconfiguration.elements

import org.springframework.http.HttpStatus
import java.lang.Exception

class InternalException(val status: HttpStatus, text: String): Exception(text)