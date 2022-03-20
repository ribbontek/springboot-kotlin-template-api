package com.ribbontek.template.service

import com.ribbontek.template.exception.model.QuestNotFoundException
import com.ribbontek.template.mapping.toQuest
import com.ribbontek.template.model.QuestView
import com.ribbontek.template.repository.QuestRepository
import com.ribbontek.template.repository.model.QuestEntity
import com.ribbontek.template.util.QuestFactory
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

internal class QuestServiceTest {

    private lateinit var questService: QuestService

    private val questRepository: QuestRepository = mock()

    @BeforeEach
    internal fun setUp() {
        questService = QuestServiceImpl(questRepository)
    }

    @Test
    fun `findQuestById - success`() {
        val quest = QuestFactory.questEntity()
        whenever(questRepository.findQuestEntityByQuestId(any())).thenReturn(quest)

        val result = questService.findQuestById(quest.questId)

        verify(questRepository, times(1)).findQuestEntityByQuestId(any())
        quest.toQuest().assertEquals(result)
    }

    @Test
    fun `findQuestById - failure`() {
        val questId = UUID.randomUUID()
        whenever(questRepository.findQuestEntityByQuestId(questId)).thenReturn(null)

        assertThrows<QuestNotFoundException> {
            questService.findQuestById(questId)
        }
        verify(questRepository, times(1)).findQuestEntityByQuestId(any())
    }

    @Test
    fun `retrieveQuests - success`() {
        val map = QuestFactory.questEntity()
        whenever(questRepository.findAll()).thenReturn(listOf(map))

        val result = questService.retrieveQuests()

        verify(questRepository, times(1)).findAll()
        assertThat(result.size, equalTo(1))
        map.toQuest().assertEquals(result.first())
    }

    @Test
    fun `retrieveQuests - empty`() {
        whenever(questRepository.findAll()).thenReturn(emptyList<QuestEntity>())
        val result = questService.retrieveQuests()
        verify(questRepository, times(1)).findAll()
        assertThat(result.size, equalTo(0))
    }

    private fun QuestView.assertEquals(expected: QuestView) {
        assertThat(questId, equalTo(expected.questId))
        assertThat(name, equalTo(expected.name))
        assertThat(description, equalTo(expected.description))
        assertThat(level, equalTo(expected.level))
        assertThat(status, equalTo(expected.status))
        assertThat(createdUtc, equalTo(expected.createdUtc))
        assertThat(updatedUtc, equalTo(expected.updatedUtc))
    }
}
