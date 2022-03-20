package com.ribbontek.template.controller

import com.ribbontek.template.context.IntegrationTest
import com.ribbontek.template.controller.resource.QuestResource
import com.ribbontek.template.exception.model.QuestNotFoundException
import com.ribbontek.template.generator.MapGenerator
import com.ribbontek.template.generator.QuestGenerator
import com.ribbontek.template.model.DeleteQuestCommand
import com.ribbontek.template.model.PagingRequest
import com.ribbontek.template.model.QuestViewQuery
import com.ribbontek.template.repository.QuestEventStoreRepository
import com.ribbontek.template.repository.event.EventTypeEnum
import com.ribbontek.template.util.QuestFactory
import com.ribbontek.template.util.andPrint
import com.ribbontek.template.util.andStatusIsCreated
import com.ribbontek.template.util.andStatusIsNotFound
import com.ribbontek.template.util.andStatusIsOk
import com.ribbontek.template.util.buildMockMvc
import com.ribbontek.template.util.withJsonContent
import com.ribbontek.template.util.withJsonEmpty
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.containsInAnyOrder
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.greaterThan
import org.hamcrest.Matchers.greaterThanOrEqualTo
import org.hamcrest.Matchers.notNullValue
import org.hamcrest.Matchers.nullValue
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.web.context.WebApplicationContext
import java.util.UUID
import javax.inject.Inject

@IntegrationTest
class QuestControllerIntegTest {

    @Inject
    private lateinit var context: WebApplicationContext

    @Inject
    private lateinit var questGenerator: QuestGenerator

    @Inject
    private lateinit var mapGenerator: MapGenerator

    @Inject
    private lateinit var questEventStoreRepository: QuestEventStoreRepository

    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setUp() {
        mockMvc = buildMockMvc(context)
    }

    @Test
    fun `get quest - success`() {
        val quest = questGenerator.createQuest()

        mockMvc.get(QuestResource.PATH + "/{id}", quest.questId)
            .andPrint()
            .andStatusIsOk()
            .andExpect {
                jsonPath("$.questId", equalTo(quest.questId.toString()))
                jsonPath("$.name", equalTo(quest.name))
                jsonPath("$.description", equalTo(quest.description))
                jsonPath("$.level", equalTo(quest.level.toString()))
                jsonPath("$.status", equalTo(quest.status.toString()))
                jsonPath("$.createdUtc", notNullValue())
                jsonPath("$.updatedUtc", notNullValue())
            }

        val events = questEventStoreRepository.findAllByEntityUUID(quest.questId)
        assertThat(events.size, equalTo(1))
        with(events.first()) {
            assertNull(old)
            assertThat(new?.questId, equalTo(quest.questId))
            assertThat(new?.name, equalTo(quest.name))
            assertThat(new?.description, equalTo(quest.description))
            assertThat(new?.level, equalTo(quest.level))
            assertThat(new?.status, equalTo(quest.status))
        }
    }

    @Test
    fun `get quest - not found`() {
        val questId = UUID.randomUUID()

        mockMvc.get(QuestResource.PATH + "/{id}", questId)
            .andPrint()
            .andStatusIsNotFound()
            .andExpect {
                val exception = QuestNotFoundException(questId)
                jsonPath("$.code", equalTo(exception.code.name))
                jsonPath("$.message", equalTo(exception.message))
            }
    }

    @Test
    fun `get quests`() {
        questGenerator.createQuest()

        mockMvc.get(QuestResource.PATH)
            .andPrint()
            .andStatusIsOk()
            .andExpect {
                jsonPath("$.size()", greaterThan(0))
            }
    }

    @Test
    fun `create quest - success`() {
        val quest = QuestFactory.createQuestCommand()

        mockMvc.post(QuestResource.PATH + "/_create") {
            withJsonContent(quest)
        }
            .andPrint()
            .andStatusIsCreated()

        val events = questEventStoreRepository.findAllByEntityUUID(quest.questId)
        assertThat(events.size, equalTo(1))
        with(events.first { it.eventType == EventTypeEnum.CREATE }) {
            assertThat(entityType, equalTo("QUEST"))
            assertNull(old)
            assertThat(new?.questId, equalTo(quest.questId))
            assertThat(new?.name, equalTo(quest.name))
            assertThat(new?.description, equalTo(quest.description))
            assertThat(new?.level, equalTo(quest.level))
            assertThat(new?.status, equalTo(quest.status))
        }
    }

    @Test
    fun `delete quest - success`() {
        val quest = questGenerator.createQuest()

        mockMvc.post(QuestResource.PATH + "/_delete") {
            withJsonContent(DeleteQuestCommand(questId = quest.questId))
        }
            .andPrint()
            .andStatusIsOk()

        mockMvc.get(QuestResource.PATH + "/{id}", quest.questId)
            .andPrint()
            .andStatusIsNotFound()

        val events = questEventStoreRepository.findAllByEntityUUID(quest.questId)
        assertThat(events.size, equalTo(2))
        with(events.first { it.eventType == EventTypeEnum.CREATE }) {
            assertThat(entityType, equalTo("QUEST"))
            assertNull(old)
            assertThat(new?.questId, equalTo(quest.questId))
            assertThat(new?.name, equalTo(quest.name))
            assertThat(new?.description, equalTo(quest.description))
            assertThat(new?.level, equalTo(quest.level))
            assertThat(new?.status, equalTo(quest.status))
        }
        with(events.first { it.eventType == EventTypeEnum.DELETE }) {
            assertThat(entityType, equalTo("QUEST"))
            assertNotNull(old)
            assertNull(new)
        }
    }

    @Test
    fun `update quest - success`() {
        val quest = questGenerator.createQuest()
        val updateQuestCommand = QuestFactory.updateQuestCommand(quest.questId)

        mockMvc.post(QuestResource.PATH + "/_update") {
            withJsonContent(updateQuestCommand)
        }
            .andPrint()
            .andStatusIsOk()

        val events = questEventStoreRepository.findAllByEntityUUID(quest.questId)
        assertThat(events.size, equalTo(2))
        with(events.first { it.eventType == EventTypeEnum.CREATE }) {
            assertThat(entityType, equalTo("QUEST"))
            assertNull(old)
            assertThat(new?.questId, equalTo(quest.questId))
            assertThat(new?.name, equalTo(quest.name))
            assertThat(new?.description, equalTo(quest.description))
            assertThat(new?.level, equalTo(quest.level))
            assertThat(new?.status, equalTo(quest.status))
        }
        with(events.first { it.eventType == EventTypeEnum.UPDATE }) {
            assertThat(entityType, equalTo("QUEST"))
            assertNotNull(old)
            assertThat(new?.questId, equalTo(updateQuestCommand.questId))
            assertThat(new?.name, equalTo(updateQuestCommand.name))
            assertThat(new?.description, equalTo(updateQuestCommand.description))
            assertThat(new?.level, equalTo(updateQuestCommand.level))
            assertThat(new?.status, equalTo(updateQuestCommand.status))
        }
    }

    @Test
    fun `add or remove maps to quest - success`() {
        val quest = questGenerator.createQuest()
        val mapIds = (1..10).map { mapGenerator.createMap().mapId }
        val updateMapsOnQuestCommand = QuestFactory.updateMapsOnQuestCommand(questId = quest.questId, mapIds = mapIds)

        mockMvc.post(QuestResource.PATH + "/_update-maps-on-quest") {
            withJsonContent(updateMapsOnQuestCommand)
        }
            .andPrint()
            .andStatusIsOk()

        mockMvc.get(QuestResource.PATH + "/{id}", updateMapsOnQuestCommand.questId)
            .andPrint()
            .andStatusIsOk()
            .andExpect {
                jsonPath("$.questId", equalTo(updateMapsOnQuestCommand.questId.toString()))
                jsonPath("$.name", equalTo(quest.name))
                jsonPath("$.description", equalTo(quest.description))
                jsonPath("$.level", equalTo(quest.level.toString()))
                jsonPath("$.status", equalTo(quest.status.toString()))
                jsonPath("$.maps.size()", equalTo(updateMapsOnQuestCommand.mapIds!!.size))
                jsonPath("$.maps[*].mapId", containsInAnyOrder(*updateMapsOnQuestCommand.mapIds!!.map { it.toString() }.toTypedArray()))
                jsonPath("$.createdUtc", notNullValue())
                jsonPath("$.updatedUtc", notNullValue())
            }

        var events = questEventStoreRepository.findAllByEntityUUID(quest.questId)
        assertThat(events.size, equalTo(2))
        with(events.first { it.eventType == EventTypeEnum.CREATE }) {
            assertThat(entityType, equalTo("QUEST"))
            assertNull(old)
            assertThat(new?.questId, equalTo(quest.questId))
            assertThat(new?.name, equalTo(quest.name))
            assertThat(new?.description, equalTo(quest.description))
            assertThat(new?.level, equalTo(quest.level))
            assertThat(new?.status, equalTo(quest.status))
            assertThat(new?.mapIds, nullValue())
        }
        with(events.first { it.eventType == EventTypeEnum.UPDATE }) {
            assertThat(entityType, equalTo("QUEST"))
            assertNotNull(old)
            assertThat(new?.questId, equalTo(updateMapsOnQuestCommand.questId))
            assertThat(new?.name, equalTo(quest.name))
            assertThat(new?.description, equalTo(quest.description))
            assertThat(new?.level, equalTo(quest.level))
            assertThat(new?.status, equalTo(quest.status))
            assertThat(new?.mapIds, notNullValue())
            assertThat(new?.mapIds, containsInAnyOrder(*updateMapsOnQuestCommand.mapIds!!.toTypedArray()))
        }

        mockMvc.post(QuestResource.PATH + "/_update-maps-on-quest") {
            withJsonContent(updateMapsOnQuestCommand.copy(mapIds = null))
        }
            .andPrint()
            .andStatusIsOk()

        mockMvc.get(QuestResource.PATH + "/{id}", updateMapsOnQuestCommand.questId)
            .andPrint()
            .andStatusIsOk()
            .andExpect {
                jsonPath("$.questId", equalTo(updateMapsOnQuestCommand.questId.toString()))
                jsonPath("$.name", equalTo(quest.name))
                jsonPath("$.description", equalTo(quest.description))
                jsonPath("$.level", equalTo(quest.level.toString()))
                jsonPath("$.status", equalTo(quest.status.toString()))
                jsonPath("$.maps.size()", equalTo(0))
                jsonPath("$.createdUtc", notNullValue())
                jsonPath("$.updatedUtc", notNullValue())
            }

        events = questEventStoreRepository.findAllByEntityUUID(quest.questId)
        assertThat(events.size, equalTo(3))
        with(events.filter { it.eventType == EventTypeEnum.UPDATE }.maxByOrNull { it.createdUtc!! }!!) {
            assertThat(entityType, equalTo("QUEST"))
            assertNotNull(old)
            assertThat(new?.questId, equalTo(updateMapsOnQuestCommand.questId))
            assertThat(new?.name, equalTo(quest.name))
            assertThat(new?.description, equalTo(quest.description))
            assertThat(new?.level, equalTo(quest.level))
            assertThat(new?.status, equalTo(quest.status))
            assertThat(new?.mapIds, nullValue())
        }
    }

    @Test
    fun `get paged quests`() {
        val quests = (1..10).map { questGenerator.createQuest() }
        val req = PagingRequest.DEFAULT
        mockMvc.post(QuestResource.PATH + "/_query") {
            withJsonContent(req)
        }
            .andPrint()
            .andStatusIsOk()
            .andExpect {
                jsonPath("$.content.size()", equalTo(req.pageSize))
                jsonPath("$.size", equalTo(req.pageSize))
                jsonPath("$.totalElements", greaterThan(req.pageSize))
                jsonPath("$.totalPages", greaterThan(1))
            }

        val randomQuest = quests.random()
        val questQuery = PagingRequest.DEFAULT.copy(
            query = QuestViewQuery(
                name = randomQuest.name
            )
        )

        mockMvc.post(QuestResource.PATH + "/_query") {
            withJsonContent(questQuery)
        }
            .andPrint()
            .andStatusIsOk()
            .andExpect {
                jsonPath("$.content.size()", equalTo(1))
                jsonPath("$.size", equalTo(10))
                jsonPath("$.totalElements", equalTo(1))
                jsonPath("$.totalPages", equalTo(1))
            }

        val (status, count) = quests.distinctBy { it.status }
            .map { quest -> quest.status to quests.count { it.status == quest.status } }
            .maxByOrNull { it.second }!!
        val questStatusQuery = PagingRequest.DEFAULT.copy(
            query = QuestViewQuery(
                status = status
            )
        )

        mockMvc.post(QuestResource.PATH + "/_query") {
            withJsonContent(questStatusQuery)
        }
            .andPrint()
            .andStatusIsOk()
            .andExpect {
                jsonPath("$.content.size()", greaterThanOrEqualTo(count))
                jsonPath("$.size", equalTo(questStatusQuery.pageSize))
                jsonPath("$.totalElements", greaterThanOrEqualTo(count))
                jsonPath("$.totalPages", greaterThanOrEqualTo(1))
            }

        mockMvc.post(QuestResource.PATH + "/_query") {
            withJsonEmpty()
        }
            .andPrint()
            .andStatusIsOk()
            .andExpect {
                jsonPath("$.content.size()", equalTo(req.pageSize))
                jsonPath("$.size", equalTo(req.pageSize))
                jsonPath("$.totalElements", greaterThan(req.pageSize))
                jsonPath("$.totalPages", greaterThan(1))
            }
    }
}
