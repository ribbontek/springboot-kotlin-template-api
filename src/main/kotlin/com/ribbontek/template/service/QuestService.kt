package com.ribbontek.template.service

import com.ribbontek.template.mapping.toQuest
import com.ribbontek.template.model.PagingRequest
import com.ribbontek.template.model.QuestView
import com.ribbontek.template.model.QuestViewQuery
import com.ribbontek.template.repository.QuestRepository
import com.ribbontek.template.repository.expectOneByQuestId
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

interface QuestService {
    fun findQuestById(questId: UUID): QuestView
    fun retrieveQuests(): List<QuestView>
    fun retrieveQuests(pageable: PagingRequest<QuestViewQuery>?): Page<QuestView>
}

@Service
class QuestServiceImpl(
    private val questRepository: QuestRepository
) : QuestService {

    @Transactional(readOnly = true)
    override fun findQuestById(questId: UUID): QuestView {
        return questRepository.expectOneByQuestId(questId).toQuest()
    }

    @Transactional(readOnly = true)
    override fun retrieveQuests(): List<QuestView> {
        return questRepository.findAll().map { it.toQuest() }
    }

    @Transactional(readOnly = true)
    override fun retrieveQuests(pageable: PagingRequest<QuestViewQuery>?): Page<QuestView> {
        return when (pageable?.query) {
            null -> questRepository.findAll(PagingRequest.DEFAULT)
            else -> questRepository.findAllByNameOrStatus(pageable.query.name, pageable.query.status, pageable)
        }.map { it.toQuest() }
    }
}
