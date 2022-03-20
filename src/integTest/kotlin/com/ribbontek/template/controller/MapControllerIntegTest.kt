package com.ribbontek.template.controller

import com.ribbontek.template.context.IntegrationTest
import com.ribbontek.template.controller.resource.MapResource
import com.ribbontek.template.exception.model.MapNotFoundException
import com.ribbontek.template.generator.MapGenerator
import com.ribbontek.template.model.DeleteMapCommand
import com.ribbontek.template.util.MapFactory
import com.ribbontek.template.util.andPrint
import com.ribbontek.template.util.andStatusIsCreated
import com.ribbontek.template.util.andStatusIsNotFound
import com.ribbontek.template.util.andStatusIsOk
import com.ribbontek.template.util.buildMockMvc
import com.ribbontek.template.util.withJsonContent
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.greaterThan
import org.hamcrest.Matchers.notNullValue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.web.context.WebApplicationContext
import java.util.UUID
import javax.inject.Inject

@IntegrationTest
class MapControllerIntegTest {

    @Inject
    private lateinit var context: WebApplicationContext

    @Inject
    private lateinit var mapGenerator: MapGenerator

    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setUp() {
        mockMvc = buildMockMvc(context)
    }

    @Test
    fun `get map - success`() {
        val map = mapGenerator.createMap()

        mockMvc.get(MapResource.PATH + "/{id}", map.mapId)
            .andPrint()
            .andStatusIsOk()
            .andExpect {
                jsonPath("$.mapId", equalTo(map.mapId.toString()))
                jsonPath("$.name", equalTo(map.name))
                jsonPath("$.urlLocation", equalTo(map.urlLocation))
                jsonPath("$.createdUtc", notNullValue())
            }
    }

    @Test
    fun `get map - not found`() {
        val mapId = UUID.randomUUID()

        mockMvc.get(MapResource.PATH + "/{id}", mapId)
            .andPrint()
            .andStatusIsNotFound()
            .andExpect {
                val exception = MapNotFoundException(mapId)
                jsonPath("$.code", equalTo(exception.code.name))
                jsonPath("$.message", equalTo(exception.message))
            }
    }

    @Test
    fun `get maps`() {
        mapGenerator.createMap()

        mockMvc.get(MapResource.PATH)
            .andPrint()
            .andStatusIsOk()
            .andExpect {
                jsonPath("$.size()", greaterThan(0))
            }
    }

    @Test
    fun `create map - success`() {
        val createMapCommand = MapFactory.createMapCommand()

        mockMvc.post(MapResource.PATH + "/_create") {
            withJsonContent(createMapCommand)
        }
            .andPrint()
            .andStatusIsCreated()
    }

    @Test
    fun `update map - success`() {
        val map = mapGenerator.createMap()
        val updateMapCommand = MapFactory.updateMapCommand(map.mapId)

        mockMvc.post(MapResource.PATH + "/_update") {
            withJsonContent(updateMapCommand)
        }
            .andPrint()
            .andStatusIsOk()
    }

    @Test
    fun `delete map - success`() {
        val map = mapGenerator.createMap()

        mockMvc.post(MapResource.PATH + "/_delete") {
            withJsonContent(DeleteMapCommand(mapId = map.mapId))
        }
            .andPrint()
            .andStatusIsOk()

        mockMvc.get(MapResource.PATH + "/{id}", map.mapId)
            .andPrint()
            .andStatusIsNotFound()
    }
}
