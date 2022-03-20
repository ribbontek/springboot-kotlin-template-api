package com.ribbontek.template.controller

import com.ribbontek.template.controller.resource.MapResource
import com.ribbontek.template.model.CreateMapCommand
import com.ribbontek.template.model.DeleteMapCommand
import com.ribbontek.template.model.MapView
import com.ribbontek.template.model.UpdateMapCommand
import com.ribbontek.template.service.MapService
import org.springframework.context.ApplicationEventPublisher
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping(MapResource.PATH)
class MapController(
    private val publisher: ApplicationEventPublisher,
    private val mapService: MapService
) : MapResource {
    override fun createMap(command: CreateMapCommand) {
        publisher.publishEvent(command)
    }

    override fun updateMap(command: UpdateMapCommand) {
        publisher.publishEvent(command)
    }

    override fun getMap(mapId: UUID): MapView {
        return mapService.findMapById(mapId)
    }

    override fun retrieveMaps(): List<MapView> {
        return mapService.retrieveMaps()
    }

    override fun deleteMap(command: DeleteMapCommand) {
        publisher.publishEvent(command)
    }
}
