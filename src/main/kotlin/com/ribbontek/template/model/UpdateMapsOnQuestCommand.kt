package com.ribbontek.template.model

import com.ribbontek.template.model.validator.NoDuplicates
import com.ribbontek.template.model.validator.ValidMapIdList
import io.swagger.v3.oas.annotations.media.Schema
import java.util.UUID

@Schema(description = "Updates Maps on a Quest Command Model")
data class UpdateMapsOnQuestCommand(

    @Schema(description = "The UUID of the quest")
    val questId: UUID,

    @Schema(description = "The list of Map UUIDs to associate with the quest")
    @get:[NoDuplicates ValidMapIdList]
    val mapIds: List<UUID>? = null
) : QuestCommand
