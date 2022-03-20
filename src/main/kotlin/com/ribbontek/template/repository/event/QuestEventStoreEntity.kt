package com.ribbontek.template.repository.event

import com.ribbontek.template.model.domain.QuestDomain
import javax.persistence.DiscriminatorValue
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@DiscriminatorValue("QUEST")
@Table(name = "event_store", schema = "ribbontek")
class QuestEventStoreEntity(override val entityType: String = "QUEST") : AbstractEventStoreEntity<QuestDomain>()
