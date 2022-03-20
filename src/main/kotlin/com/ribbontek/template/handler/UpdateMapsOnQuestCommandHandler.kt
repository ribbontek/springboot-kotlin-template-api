package com.ribbontek.template.handler

import com.ribbontek.template.aspect.Logging
import com.ribbontek.template.aspect.Projector
import com.ribbontek.template.mapping.toEventStore
import com.ribbontek.template.mapping.toProjection
import com.ribbontek.template.model.UpdateMapsOnQuestCommand
import com.ribbontek.template.model.domain.Projection
import com.ribbontek.template.model.domain.QuestDomain
import com.ribbontek.template.repository.QuestEventStoreRepository
import com.ribbontek.template.repository.expectPreviousEventByQuestId
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UpdateMapsOnQuestCommandHandler(
    private val questEventStoreRepository: QuestEventStoreRepository
) : CommandHandler<UpdateMapsOnQuestCommand, QuestDomain> {

    @Logging
    @Projector
    @EventListener
    @Transactional
    override fun handle(command: UpdateMapsOnQuestCommand): Projection<QuestDomain> {
        val previousEvent = questEventStoreRepository.expectPreviousEventByQuestId(command.questId)
        return questEventStoreRepository.saveAndFlush(command.toEventStore(previousEvent.new!!)).toProjection()
    }
}
