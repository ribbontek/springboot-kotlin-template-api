package com.ribbontek.template.generator

import com.ribbontek.template.model.QuestView
import com.ribbontek.template.util.QuestFactory
import org.springframework.stereotype.Service

interface QuestGenerator {
    fun createQuest(): QuestView
}

@Service
class QuestGeneratorImpl(
    private val questAPIClient: QuestAPIClient
) : QuestGenerator {

    override fun createQuest(): QuestView {
        val command = QuestFactory.createQuestCommand()
        questAPIClient.createQuest(command)
        return questAPIClient.getQuest(command.questId)
    }
}
