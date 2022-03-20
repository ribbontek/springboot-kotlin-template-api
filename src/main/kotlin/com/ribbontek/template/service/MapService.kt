package com.ribbontek.template.service

import com.ribbontek.template.mapping.toQuestMap
import com.ribbontek.template.model.MapView
import com.ribbontek.template.repository.MapRepository
import com.ribbontek.template.repository.expectOneByMapId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

interface MapService {
    fun findMapById(mapId: UUID): MapView
    fun retrieveMaps(): List<MapView>
}

@Service
class MapServiceImpl(
    private val mapRepository: MapRepository
) : MapService {
    @Transactional(readOnly = true)
    override fun findMapById(mapId: UUID): MapView {
        return mapRepository.expectOneByMapId(mapId).toQuestMap()
    }

    @Transactional(readOnly = true)
    override fun retrieveMaps(): List<MapView> {
        return mapRepository.findAll().map { it.toQuestMap() }
    }
}
