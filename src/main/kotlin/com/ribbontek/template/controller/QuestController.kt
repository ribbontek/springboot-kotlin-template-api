package com.ribbontek.template.controller

import com.ribbontek.template.controller.resource.QuestResource
import com.ribbontek.template.model.CreateQuestCommand
import com.ribbontek.template.model.DeleteQuestCommand
import com.ribbontek.template.model.PagingRequest
import com.ribbontek.template.model.QuestView
import com.ribbontek.template.model.QuestViewQuery
import com.ribbontek.template.model.UpdateMapsOnQuestCommand
import com.ribbontek.template.model.UpdateQuestCommand
import com.ribbontek.template.service.QuestService
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.domain.Page
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping(QuestResource.PATH)
class QuestController(
    private val publisher: ApplicationEventPublisher,
    private val questService: QuestService
) : QuestResource {
    override fun createQuest(command: CreateQuestCommand) {
        publisher.publishEvent(command)
    }

    override fun updateQuest(command: UpdateQuestCommand) {
        publisher.publishEvent(command)
    }

    override fun updateMapsOnQuest(command: UpdateMapsOnQuestCommand) {
        publisher.publishEvent(command)
    }

    override fun getQuest(questId: UUID): QuestView {
        return questService.findQuestById(questId)
    }

    override fun retrieveQuests(): List<QuestView> {
        return questService.retrieveQuests()
    }

    override fun retrieveQuests(pagingRequest: PagingRequest<QuestViewQuery>?): Page<QuestView> {
        return questService.retrieveQuests(pagingRequest)
    }

    override fun deleteQuest(command: DeleteQuestCommand) {
        publisher.publishEvent(command)
    }
}
