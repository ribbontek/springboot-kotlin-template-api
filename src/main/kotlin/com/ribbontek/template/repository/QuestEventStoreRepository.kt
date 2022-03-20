package com.ribbontek.template.repository

import com.ribbontek.template.exception.model.QuestNotFoundException
import com.ribbontek.template.repository.event.EventTypeEnum
import com.ribbontek.template.repository.event.QuestEventStoreEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface QuestEventStoreRepository : JpaRepository<QuestEventStoreEntity, Long> {
    fun findFirstByEntityUUIDAndEventTypeIsNotOrderByCreatedUtcDesc(
        questId: UUID,
        eventTypeEnum: EventTypeEnum
    ): QuestEventStoreEntity?

    fun findAllByEntityUUID(questId: UUID): List<QuestEventStoreEntity>
}

fun QuestEventStoreRepository.expectPreviousEventByQuestId(questId: UUID): QuestEventStoreEntity {
    return findFirstByEntityUUIDAndEventTypeIsNotOrderByCreatedUtcDesc(questId, EventTypeEnum.DELETE)
        ?: throw QuestNotFoundException(questId)
}
