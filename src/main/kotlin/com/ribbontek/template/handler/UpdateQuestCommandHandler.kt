package com.ribbontek.template.handler

import com.ribbontek.template.aspect.Logging
import com.ribbontek.template.aspect.Projector
import com.ribbontek.template.mapping.toEventStore
import com.ribbontek.template.mapping.toProjection
import com.ribbontek.template.model.UpdateQuestCommand
import com.ribbontek.template.model.domain.Projection
import com.ribbontek.template.model.domain.QuestDomain
import com.ribbontek.template.repository.QuestEventStoreRepository
import com.ribbontek.template.repository.expectPreviousEventByQuestId
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UpdateQuestCommandHandler(
    private val questEventStoreRepository: QuestEventStoreRepository
) : CommandHandler<UpdateQuestCommand, QuestDomain> {

    @Logging
    @Projector
    @EventListener
    @Transactional
    override fun handle(command: UpdateQuestCommand): Projection<QuestDomain> {
        val previousEvent = questEventStoreRepository.expectPreviousEventByQuestId(command.questId)
        return questEventStoreRepository.saveAndFlush(command.toEventStore(previousEvent.new!!)).toProjection()
    }
}
