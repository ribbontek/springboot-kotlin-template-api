package com.ribbontek.template.repository

import com.ribbontek.template.exception.model.MapNotFoundException
import com.ribbontek.template.repository.model.MapEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface MapRepository : JpaRepository<MapEntity, Long> {
    fun findMapEntityByMapId(mapId: UUID): MapEntity?
    fun existsByMapId(mapId: UUID): Boolean
    fun existsByMapIdOrName(mapId: UUID, name: String): Boolean
    fun findAllByMapIdIn(mapIds: List<UUID>): List<MapEntity>
}

fun MapRepository.expectOneByMapId(mapId: UUID): MapEntity {
    return findMapEntityByMapId(mapId) ?: throw MapNotFoundException(mapId)
}
