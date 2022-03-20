package com.ribbontek.template.exception

import com.ribbontek.template.exception.model.ApiException
import com.ribbontek.template.exception.model.GenericException
import com.ribbontek.template.exception.model.MapNotFoundException
import com.ribbontek.template.exception.model.QuestNotFoundException
import com.ribbontek.template.exception.model.toApiExceptionResponseEntity
import com.ribbontek.template.util.logger
import org.slf4j.Logger
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import javax.validation.ConstraintViolationException

@ControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {

    private val log: Logger = logger()

    @ExceptionHandler
    fun handleException(ex: Exception): ResponseEntity<ApiException> {
        return when (ex) {
            is ConstraintViolationException -> ex.toApiExceptionResponseEntity()
            is QuestNotFoundException -> ex.toApiExceptionResponseEntity()
            is MapNotFoundException -> ex.toApiExceptionResponseEntity()
            else -> {
                log.error("Unhandled exception", ex)
                GenericException().toApiExceptionResponseEntity()
            }
        }
    }
}
