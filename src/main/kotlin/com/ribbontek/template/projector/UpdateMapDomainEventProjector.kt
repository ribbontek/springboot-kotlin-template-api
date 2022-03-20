package com.ribbontek.template.projector

import com.ribbontek.template.aspect.Logging
import com.ribbontek.template.model.domain.UpdateQuestMapDomainEvent
import com.ribbontek.template.repository.MapRepository
import com.ribbontek.template.repository.expectOneByMapId
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UpdateMapDomainEventProjector(
    private val mapRepository: MapRepository
) : EventProjector<UpdateQuestMapDomainEvent> {

    @Logging
    @EventListener
    @Transactional
    override fun project(event: UpdateQuestMapDomainEvent) {
        mapRepository.expectOneByMapId(event.domain.mapId).update(event.domain)
    }
}
