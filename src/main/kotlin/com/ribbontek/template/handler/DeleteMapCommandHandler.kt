package com.ribbontek.template.handler

import com.ribbontek.template.aspect.Logging
import com.ribbontek.template.aspect.Projector
import com.ribbontek.template.mapping.toEventStore
import com.ribbontek.template.mapping.toProjectionDeleted
import com.ribbontek.template.model.DeleteMapCommand
import com.ribbontek.template.model.domain.Projection
import com.ribbontek.template.model.domain.QuestMapDomain
import com.ribbontek.template.repository.MapEventStoreRepository
import com.ribbontek.template.repository.expectPreviousEventByMapIdAndNotDeleted
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DeleteMapCommandHandler(
    private val mapEventStoreRepository: MapEventStoreRepository
) : CommandHandler<DeleteMapCommand, QuestMapDomain> {

    @Logging
    @Projector
    @EventListener
    @Transactional
    override fun handle(command: DeleteMapCommand): Projection<QuestMapDomain> {
        val previousEvent = mapEventStoreRepository.expectPreviousEventByMapIdAndNotDeleted(command.mapId)
        return mapEventStoreRepository.saveAndFlush(command.toEventStore(previousEvent.new!!)).toProjectionDeleted()
    }
}
