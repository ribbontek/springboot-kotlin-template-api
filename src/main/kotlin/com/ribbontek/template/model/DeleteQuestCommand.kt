package com.ribbontek.template.model

import io.swagger.v3.oas.annotations.media.Schema
import java.util.UUID

@Schema(description = "The Delete Quest Command Model")
data class DeleteQuestCommand(

    @Schema(description = "The UUID of the quest")
    val questId: UUID
) : QuestCommand
