package com.ribbontek.template.model.domain

import java.util.UUID

data class QuestMapDomain(
    val mapId: UUID,
    val name: String,
    val urlLocation: String? = null
) : Domain
