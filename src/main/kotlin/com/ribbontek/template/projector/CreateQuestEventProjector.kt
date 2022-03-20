package com.ribbontek.template.projector

import com.ribbontek.template.aspect.Logging
import com.ribbontek.template.mapping.toQuestEntity
import com.ribbontek.template.model.domain.CreateQuestDomainEvent
import com.ribbontek.template.repository.QuestRepository
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CreateQuestEventProjector(
    private val questRepository: QuestRepository
) : EventProjector<CreateQuestDomainEvent> {

    @Logging
    @EventListener
    @Transactional
    override fun project(event: CreateQuestDomainEvent) {
        questRepository.saveAndFlush(event.domain.toQuestEntity())
    }
}
