package com.ribbontek.template.model

import io.swagger.v3.oas.annotations.media.Schema
import java.util.UUID
import javax.validation.constraints.NotBlank

@Schema(description = "The Update Map Command Model")
data class UpdateMapCommand(
    @Schema(description = "The UUID of the map")
    val mapId: UUID,
    @Schema(description = "The name of the map")
    @NotBlank(message = "The name cannot be blank / empty")
    val name: String,
    @Schema(description = "The url location for the map")
    val urlLocation: String? = null
) : MapCommand
