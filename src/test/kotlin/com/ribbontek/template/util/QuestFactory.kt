package com.ribbontek.template.util

import com.ribbontek.template.model.CreateQuestCommand
import com.ribbontek.template.model.UpdateMapsOnQuestCommand
import com.ribbontek.template.model.UpdateQuestCommand
import com.ribbontek.template.repository.model.QuestEntity
import java.time.ZonedDateTime
import java.util.UUID

object QuestFactory {

    fun questEntity() =
        QuestEntity(
            questId = UUID.randomUUID(),
            name = FakerUtil.alphaNumeric(100),
            description = FakerUtil.alphaNumeric(255),
            level = FakerUtil.enum(),
            status = FakerUtil.enum()
        ).apply {
            createdUtc = ZonedDateTime.now()
            updatedUtc = ZonedDateTime.now()
        }

    fun createQuestCommand() = CreateQuestCommand(
        questId = UUID.randomUUID(),
        name = FakerUtil.alphaNumeric(100),
        description = FakerUtil.alphaNumeric(255),
        level = FakerUtil.enum(),
        status = FakerUtil.enum()
    )

    fun updateQuestCommand(questId: UUID) = UpdateQuestCommand(
        questId = questId,
        name = FakerUtil.alphaNumeric(100),
        description = FakerUtil.alphaNumeric(255),
        level = FakerUtil.enum(),
        status = FakerUtil.enum()
    )

    fun updateMapsOnQuestCommand(questId: UUID, mapIds: List<UUID>) = UpdateMapsOnQuestCommand(
        questId = questId,
        mapIds = mapIds
    )
}
