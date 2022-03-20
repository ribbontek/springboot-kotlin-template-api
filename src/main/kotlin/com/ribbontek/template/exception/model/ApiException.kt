package com.ribbontek.template.exception.model

import com.ribbontek.template.util.toUtc
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.time.ZonedDateTime
import javax.validation.ConstraintViolationException

data class ApiException(
    val code: HttpStatus,
    val message: String? = null,
    val violations: List<String>? = null,
    val time: ZonedDateTime
)

fun ConstraintViolationException.toApiExceptionResponseEntity(): ResponseEntity<ApiException> =
    ResponseEntity(this.toApiException(), HttpStatus.BAD_REQUEST)

fun ConstraintViolationException.toApiException(): ApiException =
    ApiException(
        code = HttpStatus.BAD_REQUEST,
        message = this.message ?: this.localizedMessage,
        violations = this.constraintViolations.takeIf { it.isNotEmpty() }?.map { it.message },
        time = ZonedDateTime.now().toUtc()
    )

fun AppException.toApiExceptionResponseEntity(): ResponseEntity<ApiException> =
    ResponseEntity(this.toApiException(), this.code)

fun AppException.toApiException(): ApiException =
    ApiException(code = code, message = message ?: localizedMessage, time = ZonedDateTime.now().toUtc())
