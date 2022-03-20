package com.ribbontek.template.service

import com.ribbontek.template.exception.model.MapNotFoundException
import com.ribbontek.template.mapping.toQuestMap
import com.ribbontek.template.model.MapView
import com.ribbontek.template.repository.MapRepository
import com.ribbontek.template.repository.model.MapEntity
import com.ribbontek.template.util.MapFactory
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.util.UUID

internal class MapServiceTest {

    private lateinit var mapService: MapService

    private val mapRepository: MapRepository = mock()

    @BeforeEach
    internal fun setUp() {
        mapService = MapServiceImpl(mapRepository)
    }

    @Test
    fun `findMapById - success`() {
        val map = MapFactory.mapEntity()
        whenever(mapRepository.findMapEntityByMapId(any())).thenReturn(map)

        val result = mapService.findMapById(map.mapId)

        verify(mapRepository, times(1)).findMapEntityByMapId(any())
        map.toQuestMap().assertEquals(result)
    }

    @Test
    fun `findMapById - failure`() {
        val questId = UUID.randomUUID()
        whenever(mapRepository.findMapEntityByMapId(questId)).thenReturn(null)

        assertThrows<MapNotFoundException> {
            mapService.findMapById(questId)
        }
        verify(mapRepository, times(1)).findMapEntityByMapId(any())
    }

    @Test
    fun `retrieveMaps - success`() {
        val map = MapFactory.mapEntity()
        whenever(mapRepository.findAll()).thenReturn(listOf(map))

        val result = mapService.retrieveMaps()

        verify(mapRepository, times(1)).findAll()
        assertThat(result.size, equalTo(1))
        map.toQuestMap().assertEquals(result.first())
    }

    @Test
    fun `retrieveMaps - empty`() {
        whenever(mapRepository.findAll()).thenReturn(emptyList<MapEntity>())
        val result = mapService.retrieveMaps()
        verify(mapRepository, times(1)).findAll()
        assertThat(result.size, equalTo(0))
    }

    private fun MapView.assertEquals(expected: MapView) {
        assertThat(mapId, equalTo(expected.mapId))
        assertThat(name, equalTo(expected.name))
        assertThat(urlLocation, equalTo(expected.urlLocation))
        assertThat(createdUtc, equalTo(expected.createdUtc))
        assertThat(updatedUtc, equalTo(expected.updatedUtc))
    }
}
