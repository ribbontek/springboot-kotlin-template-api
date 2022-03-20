package com.ribbontek.template.exception.model

import org.springframework.http.HttpStatus

abstract class AppException(
    val code: HttpStatus,
    override val message: String? = null
) : Exception()

class GenericException : AppException(code = HttpStatus.INTERNAL_SERVER_ERROR, message = "Something went wrong!")
