package com.ribbontek.template.exception.model

import org.springframework.http.HttpStatus
import java.util.UUID

data class MapNotFoundException(val questId: UUID) : AppException(HttpStatus.NOT_FOUND) {
    override val message: String
        get() = "Could not find Map with id $questId"
}
