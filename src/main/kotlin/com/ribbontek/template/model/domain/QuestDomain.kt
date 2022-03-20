package com.ribbontek.template.model.domain

import com.ribbontek.template.model.QuestLevel
import com.ribbontek.template.model.QuestStatus
import java.util.UUID

data class QuestDomain(
    val questId: UUID,
    val name: String,
    val description: String? = null,
    val mapIds: List<UUID>? = null,
    val level: QuestLevel,
    val status: QuestStatus,
) : Domain
