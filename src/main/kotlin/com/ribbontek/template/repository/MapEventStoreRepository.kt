package com.ribbontek.template.repository

import com.ribbontek.template.exception.model.MapNotFoundException
import com.ribbontek.template.repository.event.EventTypeEnum
import com.ribbontek.template.repository.event.MapEventStoreEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface MapEventStoreRepository : JpaRepository<MapEventStoreEntity, Long> {
    fun findFirstByEntityUUIDAndEventTypeIsNotOrderByCreatedUtcDesc(
        mapId: UUID,
        eventTypeEnum: EventTypeEnum
    ): MapEventStoreEntity?
}

fun MapEventStoreRepository.expectPreviousEventByMapIdAndNotDeleted(mapId: UUID): MapEventStoreEntity {
    return findFirstByEntityUUIDAndEventTypeIsNotOrderByCreatedUtcDesc(mapId, EventTypeEnum.DELETE)
        ?: throw MapNotFoundException(mapId)
}
