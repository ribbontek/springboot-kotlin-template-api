package com.ribbontek.template.handler

import com.ribbontek.template.aspect.Logging
import com.ribbontek.template.aspect.Projector
import com.ribbontek.template.mapping.toEventStore
import com.ribbontek.template.mapping.toProjection
import com.ribbontek.template.model.CreateQuestCommand
import com.ribbontek.template.model.domain.Projection
import com.ribbontek.template.model.domain.QuestDomain
import com.ribbontek.template.repository.QuestEventStoreRepository
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CreateQuestCommandHandler(
    private val questEventStoreRepository: QuestEventStoreRepository
) : CommandHandler<CreateQuestCommand, QuestDomain> {

    @Logging
    @Projector
    @EventListener
    @Transactional
    override fun handle(command: CreateQuestCommand): Projection<QuestDomain> {
        return questEventStoreRepository.saveAndFlush(command.toEventStore()).toProjection()
    }
}
