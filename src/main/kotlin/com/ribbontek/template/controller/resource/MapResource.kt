package com.ribbontek.template.controller.resource

import com.ribbontek.template.model.CreateMapCommand
import com.ribbontek.template.model.DeleteMapCommand
import com.ribbontek.template.model.MapView
import com.ribbontek.template.model.UpdateMapCommand
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseStatus
import java.util.UUID

interface MapResource {
    companion object {
        const val PATH = "/maps"
    }

    @Operation(summary = "Create a new map")
    @RequestMapping(
        value = ["/_create"],
        method = [RequestMethod.POST],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseStatus(HttpStatus.CREATED)
    fun createMap(@Validated @RequestBody command: CreateMapCommand)

    @Operation(summary = "Update an existing map")
    @RequestMapping(
        value = ["/_update"],
        method = [RequestMethod.POST],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun updateMap(@Validated @RequestBody command: UpdateMapCommand)

    @Operation(summary = "Get a map by id")
    @RequestMapping(
        value = ["/{mapId}"],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getMap(@PathVariable("mapId") mapId: UUID): MapView

    @Operation(summary = "Retrieve all the maps")
    @RequestMapping(
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun retrieveMaps(): List<MapView>

    @Operation(summary = "Delete an existing map")
    @RequestMapping(
        value = ["/_delete"],
        method = [RequestMethod.POST],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun deleteMap(@Validated @RequestBody command: DeleteMapCommand)
}
