package com.ribbontek.template.mapping

import com.ribbontek.template.model.CreateQuestCommand
import com.ribbontek.template.model.DeleteQuestCommand
import com.ribbontek.template.model.QuestView
import com.ribbontek.template.model.UpdateMapsOnQuestCommand
import com.ribbontek.template.model.UpdateQuestCommand
import com.ribbontek.template.model.domain.Projection
import com.ribbontek.template.model.domain.QuestDomain
import com.ribbontek.template.repository.event.EventTypeEnum
import com.ribbontek.template.repository.event.QuestEventStoreEntity
import com.ribbontek.template.repository.model.QuestEntity

fun QuestDomain.toQuestEntity() =
    QuestEntity(
        questId = questId,
        name = name,
        description = description,
        level = level,
        status = status
    )

fun CreateQuestCommand.toQuestDomain() =
    QuestDomain(
        questId = questId,
        name = name,
        description = description,
        level = level,
        status = status
    )

fun UpdateQuestCommand.toQuestDomain(oldDomain: QuestDomain) =
    QuestDomain(
        questId = questId,
        name = name,
        description = description,
        level = level,
        status = status,
        mapIds = oldDomain.mapIds
    )

fun UpdateMapsOnQuestCommand.toQuestDomain(oldDomain: QuestDomain) =
    QuestDomain(
        questId = questId,
        name = oldDomain.name,
        description = oldDomain.description,
        level = oldDomain.level,
        status = oldDomain.status,
        mapIds = mapIds
    )

fun CreateQuestCommand.toEventStore(): QuestEventStoreEntity {
    val source = this
    return QuestEventStoreEntity().apply {
        entityUUID = source.questId
        old = null
        new = source.toQuestDomain()
        eventType = EventTypeEnum.CREATE
    }
}

fun UpdateQuestCommand.toEventStore(oldDomain: QuestDomain): QuestEventStoreEntity {
    val source = this
    return QuestEventStoreEntity().apply {
        entityUUID = source.questId
        old = oldDomain
        new = source.toQuestDomain(oldDomain)
        eventType = EventTypeEnum.UPDATE
    }
}

fun UpdateMapsOnQuestCommand.toEventStore(oldDomain: QuestDomain): QuestEventStoreEntity {
    val source = this
    return QuestEventStoreEntity().apply {
        entityUUID = source.questId
        old = oldDomain
        new = source.toQuestDomain(oldDomain)
        eventType = EventTypeEnum.UPDATE
    }
}

fun DeleteQuestCommand.toEventStore(oldDomain: QuestDomain): QuestEventStoreEntity {
    val source = this
    return QuestEventStoreEntity().apply {
        entityUUID = source.questId
        old = oldDomain
        new = null
        eventType = EventTypeEnum.DELETE
    }
}

fun QuestEventStoreEntity.toProjection(): Projection<QuestDomain> =
    Projection(
        new,
        eventType!!
    )

fun QuestEventStoreEntity.toProjectionDeleted(): Projection<QuestDomain> =
    Projection(
        old,
        eventType!!
    )

fun QuestEntity.toQuest() = QuestView(
    questId = questId,
    name = name,
    description = description,
    level = level,
    status = status,
    maps = maps?.map { it.toQuestMap() } ?: emptyList(),
    createdUtc = createdUtc!!,
    updatedUtc = updatedUtc!!
)
