package com.ribbontek.template.projector

import com.ribbontek.template.aspect.Logging
import com.ribbontek.template.mapping.toMapEntity
import com.ribbontek.template.model.domain.CreateQuestMapDomainEvent
import com.ribbontek.template.repository.MapRepository
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CreateMapDomainEventProjector(
    private val mapRepository: MapRepository
) : EventProjector<CreateQuestMapDomainEvent> {

    @Logging
    @EventListener
    @Transactional
    override fun project(event: CreateQuestMapDomainEvent) {
        mapRepository.saveAndFlush(event.domain.toMapEntity())
    }
}
