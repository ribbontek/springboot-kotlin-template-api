package com.ribbontek.template.generator

import com.ribbontek.template.controller.resource.MapResource
import com.ribbontek.template.controller.resource.QuestResource
import org.springframework.cloud.openfeign.FeignClient

object ConsulAPI {
    const val NAME = "template-api"
}

@FeignClient(
    name = ConsulAPI.NAME,
    contextId = "QuestAPIClient",
    path = QuestResource.PATH
)
interface QuestAPIClient : QuestResource

@FeignClient(
    name = ConsulAPI.NAME,
    contextId = "MapAPIClient",
    path = MapResource.PATH
)
interface MapAPIClient : MapResource
