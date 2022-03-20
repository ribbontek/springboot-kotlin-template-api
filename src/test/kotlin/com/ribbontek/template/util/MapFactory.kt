package com.ribbontek.template.util

import com.ribbontek.template.model.CreateMapCommand
import com.ribbontek.template.model.UpdateMapCommand
import com.ribbontek.template.repository.model.MapEntity
import java.time.ZonedDateTime
import java.util.UUID

object MapFactory {

    fun mapEntity() =
        MapEntity(
            mapId = UUID.randomUUID(),
            name = FakerUtil.alphaNumeric(100),
            urlLocation = FakerUtil.alphaNumeric(255)
        ).apply {
            createdUtc = ZonedDateTime.now()
            updatedUtc = ZonedDateTime.now()
        }

    fun createMapCommand() = CreateMapCommand(
        mapId = UUID.randomUUID(),
        name = FakerUtil.alphaNumeric(100),
        urlLocation = FakerUtil.alphaNumeric(255)
    )

    fun updateMapCommand(mapId: UUID) = UpdateMapCommand(
        mapId = mapId,
        name = FakerUtil.alphaNumeric(100),
        urlLocation = FakerUtil.alphaNumeric(255)
    )
}
