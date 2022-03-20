package com.ribbontek.template.repository

import com.ribbontek.template.exception.model.QuestNotFoundException
import com.ribbontek.template.model.QuestStatus
import com.ribbontek.template.repository.model.QuestEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface QuestRepository : JpaRepository<QuestEntity, Long> {
    fun findQuestEntityByQuestId(questId: UUID): QuestEntity?
    fun existsByQuestIdOrName(questId: UUID, name: String): Boolean
    fun findAllByNameOrStatus(name: String?, status: QuestStatus?, pageable: Pageable): Page<QuestEntity>
}

fun QuestRepository.expectOneByQuestId(questId: UUID): QuestEntity {
    return findQuestEntityByQuestId(questId) ?: throw QuestNotFoundException(questId)
}
