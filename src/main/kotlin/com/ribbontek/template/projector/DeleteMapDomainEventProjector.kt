package com.ribbontek.template.projector

import com.ribbontek.template.aspect.Logging
import com.ribbontek.template.model.domain.DeleteQuestMapDomainEvent
import com.ribbontek.template.repository.MapRepository
import com.ribbontek.template.repository.expectOneByMapId
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DeleteMapDomainEventProjector(
    private val mapRepository: MapRepository
) : EventProjector<DeleteQuestMapDomainEvent> {

    @Logging
    @EventListener
    @Transactional
    override fun project(event: DeleteQuestMapDomainEvent) {
        mapRepository.expectOneByMapId(event.domain.mapId).also { mapRepository.delete(it) }
    }
}
