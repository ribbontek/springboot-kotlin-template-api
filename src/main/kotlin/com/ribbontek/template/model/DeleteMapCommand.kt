package com.ribbontek.template.model

import io.swagger.v3.oas.annotations.media.Schema
import java.util.UUID

@Schema(description = "The Delete Map Command Model")
data class DeleteMapCommand(
    @Schema(description = "The UUID of the map")
    val mapId: UUID
) : MapCommand
