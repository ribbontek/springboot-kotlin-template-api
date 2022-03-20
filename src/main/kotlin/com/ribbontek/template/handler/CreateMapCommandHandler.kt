package com.ribbontek.template.handler

import com.ribbontek.template.aspect.Logging
import com.ribbontek.template.aspect.Projector
import com.ribbontek.template.mapping.toEventStore
import com.ribbontek.template.mapping.toProjection
import com.ribbontek.template.model.CreateMapCommand
import com.ribbontek.template.model.domain.Projection
import com.ribbontek.template.model.domain.QuestMapDomain
import com.ribbontek.template.repository.MapEventStoreRepository
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CreateMapCommandHandler(
    private val mapEventStoreRepository: MapEventStoreRepository
) : CommandHandler<CreateMapCommand, QuestMapDomain> {

    @Logging
    @Projector
    @EventListener
    @Transactional
    override fun handle(command: CreateMapCommand): Projection<QuestMapDomain> {
        return mapEventStoreRepository.saveAndFlush(command.toEventStore()).toProjection()
    }
}
