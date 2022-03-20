package com.ribbontek.template.repository.event

import com.ribbontek.template.model.domain.QuestMapDomain
import javax.persistence.DiscriminatorValue
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@DiscriminatorValue("MAP")
@Table(name = "event_store", schema = "ribbontek")
class MapEventStoreEntity(override val entityType: String = "MAP") : AbstractEventStoreEntity<QuestMapDomain>()
