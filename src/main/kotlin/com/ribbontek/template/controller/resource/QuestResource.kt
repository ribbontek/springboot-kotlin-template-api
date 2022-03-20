package com.ribbontek.template.controller.resource

import com.ribbontek.template.model.CreateQuestCommand
import com.ribbontek.template.model.DeleteQuestCommand
import com.ribbontek.template.model.PagingRequest
import com.ribbontek.template.model.QuestView
import com.ribbontek.template.model.QuestViewQuery
import com.ribbontek.template.model.UpdateMapsOnQuestCommand
import com.ribbontek.template.model.UpdateQuestCommand
import io.swagger.v3.oas.annotations.Operation
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseStatus
import java.util.UUID

interface QuestResource {
    companion object {
        const val PATH = "/quests"
    }

    @Operation(summary = "Create a new quest")
    @RequestMapping(
        value = ["/_create"],
        method = [RequestMethod.POST],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseStatus(HttpStatus.CREATED)
    fun createQuest(@Validated @RequestBody command: CreateQuestCommand)

    @Operation(summary = "Update an existing quest")
    @RequestMapping(
        value = ["/_update"],
        method = [RequestMethod.POST],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun updateQuest(@Validated @RequestBody command: UpdateQuestCommand)

    @Operation(summary = "Updates maps on an existing quest")
    @RequestMapping(
        value = ["/_update-maps-on-quest"],
        method = [RequestMethod.POST],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun updateMapsOnQuest(@Validated @RequestBody command: UpdateMapsOnQuestCommand)

    @Operation(summary = "Get a quest by id")
    @RequestMapping(
        value = ["/{questId}"],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getQuest(@PathVariable("questId") questId: UUID): QuestView

    @Operation(summary = "Retrieve all the quests")
    @RequestMapping(
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun retrieveQuests(): List<QuestView>

    @Operation(summary = "Delete an existing quest")
    @RequestMapping(
        value = ["/_delete"],
        method = [RequestMethod.POST],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun deleteQuest(@Validated @RequestBody command: DeleteQuestCommand)

    @Operation(summary = "Retrieve quests in paged results")
    @RequestMapping(
        value = ["/_query"],
        method = [RequestMethod.POST],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun retrieveQuests(@Validated @RequestBody(required = false) pagingRequest: PagingRequest<QuestViewQuery>?): Page<QuestView>
}
