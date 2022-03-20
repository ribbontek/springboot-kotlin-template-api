package com.ribbontek.template.generator

import com.ribbontek.template.model.MapView
import com.ribbontek.template.util.MapFactory
import org.springframework.stereotype.Service

interface MapGenerator {
    fun createMap(): MapView
}

@Service
class MapGeneratorImpl(
    private val mapAPIClient: MapAPIClient
) : MapGenerator {

    override fun createMap(): MapView {
        val command = MapFactory.createMapCommand()
        mapAPIClient.createMap(command)
        return mapAPIClient.getMap(command.mapId)
    }
}
