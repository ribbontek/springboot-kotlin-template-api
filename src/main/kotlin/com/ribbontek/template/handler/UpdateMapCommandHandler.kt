package com.ribbontek.template.handler

import com.ribbontek.template.aspect.Logging
import com.ribbontek.template.aspect.Projector
import com.ribbontek.template.mapping.toEventStore
import com.ribbontek.template.mapping.toProjection
import com.ribbontek.template.model.UpdateMapCommand
import com.ribbontek.template.model.domain.Projection
import com.ribbontek.template.model.domain.QuestMapDomain
import com.ribbontek.template.repository.MapEventStoreRepository
import com.ribbontek.template.repository.expectPreviousEventByMapIdAndNotDeleted
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UpdateMapCommandHandler(
    private val mapEventStoreRepository: MapEventStoreRepository
) : CommandHandler<UpdateMapCommand, QuestMapDomain> {

    @Logging
    @Projector
    @EventListener
    @Transactional
    override fun handle(command: UpdateMapCommand): Projection<QuestMapDomain> {
        val previousEvent = mapEventStoreRepository.expectPreviousEventByMapIdAndNotDeleted(command.mapId)
        return mapEventStoreRepository.saveAndFlush(command.toEventStore(previousEvent.new!!)).toProjection()
    }
}
