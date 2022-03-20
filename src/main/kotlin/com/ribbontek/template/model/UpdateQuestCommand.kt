package com.ribbontek.template.model

import io.swagger.v3.oas.annotations.media.Schema
import java.util.UUID
import javax.validation.constraints.Size

@Schema(description = "The Create Quest Command Model")
data class UpdateQuestCommand(

    @Schema(description = "The UUID of the quest")
    val questId: UUID,

    @Schema(description = "The name of the quest")
    @Size(min = 1, max = 100, message = "The size of the quest name must be between 1 and 100")
    val name: String,

    @Schema(description = "The description of the quest")
    @Size(max = 255, message = "The size of the quest description must be between 0 and 255")
    val description: String? = null,

    @Schema(
        description = "The level of the quest",
        allowableValues = ["EASY", "MEDIUM", "HARD", "INSANE"]
    )
    val level: QuestLevel,

    @Schema(
        description = "The status of the quest",
        allowableValues = ["INACTIVE", "ACTIVE", "STARTED", "IN_PROGRESS", "COMPLETED"]
    )
    val status: QuestStatus
) : QuestCommand
