package com.ribbontek.template.mapping

import com.ribbontek.template.model.CreateMapCommand
import com.ribbontek.template.model.DeleteMapCommand
import com.ribbontek.template.model.MapView
import com.ribbontek.template.model.UpdateMapCommand
import com.ribbontek.template.model.domain.Projection
import com.ribbontek.template.model.domain.QuestMapDomain
import com.ribbontek.template.repository.event.EventTypeEnum
import com.ribbontek.template.repository.event.MapEventStoreEntity
import com.ribbontek.template.repository.model.MapEntity

fun QuestMapDomain.toMapEntity() =
    MapEntity(
        mapId = mapId,
        name = name,
        urlLocation = urlLocation
    )

fun CreateMapCommand.toMapDomain() =
    QuestMapDomain(
        mapId = mapId,
        name = name,
        urlLocation = urlLocation
    )

fun UpdateMapCommand.toMapDomain() =
    QuestMapDomain(
        mapId = mapId,
        name = name,
        urlLocation = urlLocation
    )

fun CreateMapCommand.toEventStore(): MapEventStoreEntity {
    val source = this
    return MapEventStoreEntity().apply {
        entityUUID = source.mapId
        old = null
        new = source.toMapDomain()
        eventType = EventTypeEnum.CREATE
    }
}

fun UpdateMapCommand.toEventStore(oldDomain: QuestMapDomain): MapEventStoreEntity {
    val source = this
    return MapEventStoreEntity().apply {
        entityUUID = source.mapId
        old = oldDomain
        new = source.toMapDomain()
        eventType = EventTypeEnum.UPDATE
    }
}

fun DeleteMapCommand.toEventStore(oldDomain: QuestMapDomain): MapEventStoreEntity {
    val source = this
    return MapEventStoreEntity().apply {
        entityUUID = source.mapId
        old = oldDomain
        new = null
        eventType = EventTypeEnum.DELETE
    }
}

fun MapEventStoreEntity.toProjection(): Projection<QuestMapDomain> =
    Projection(
        new,
        eventType!!
    )

fun MapEventStoreEntity.toProjectionDeleted(): Projection<QuestMapDomain> =
    Projection(
        old,
        eventType!!
    )

fun MapEntity.toQuestMap() = MapView(
    mapId = mapId,
    name = name,
    urlLocation = urlLocation,
    createdUtc = createdUtc!!,
    updatedUtc = updatedUtc!!
)
