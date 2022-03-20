package com.ribbontek.template.projector

import com.ribbontek.template.aspect.Logging
import com.ribbontek.template.model.domain.UpdateQuestDomainEvent
import com.ribbontek.template.repository.MapRepository
import com.ribbontek.template.repository.QuestRepository
import com.ribbontek.template.repository.expectOneByQuestId
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UpdateQuestEventProjector(
    private val questRepository: QuestRepository,
    private val mapRepository: MapRepository
) : EventProjector<UpdateQuestDomainEvent> {

    @Logging
    @EventListener
    @Transactional
    override fun project(event: UpdateQuestDomainEvent) {
        questRepository.expectOneByQuestId(event.domain.questId).let { quest ->
            quest.update(event.domain, event.domain.mapIds?.let { mapRepository.findAllByMapIdIn(it) }?.toMutableSet())
        }
    }
}
