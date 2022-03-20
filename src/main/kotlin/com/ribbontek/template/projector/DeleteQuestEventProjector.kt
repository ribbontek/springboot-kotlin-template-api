package com.ribbontek.template.projector

import com.ribbontek.template.aspect.Logging
import com.ribbontek.template.model.domain.DeleteQuestDomainEvent
import com.ribbontek.template.repository.QuestRepository
import com.ribbontek.template.repository.expectOneByQuestId
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DeleteQuestEventProjector(
    private val questRepository: QuestRepository
) : EventProjector<DeleteQuestDomainEvent> {

    @Logging
    @EventListener
    @Transactional
    override fun project(event: DeleteQuestDomainEvent) {
        questRepository.expectOneByQuestId(event.domain.questId).also { questRepository.delete(it) }
    }
}
