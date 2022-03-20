package com.ribbontek.template.model

import io.swagger.v3.oas.annotations.media.Schema

interface Query

@Schema(description = "The Quest View Query Model")
data class QuestViewQuery(
    @Schema(description = "Filter by the name of the quest")
    val name: String? = null,
    @Schema(description = "Filter by the status of the quest")
    val status: QuestStatus? = null
) : Query
