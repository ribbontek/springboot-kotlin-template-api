package com.ribbontek.template.model.domain

import com.ribbontek.template.repository.event.EventTypeEnum

data class CreateQuestDomainEvent(
    val domain: QuestDomain
) : DomainEvent

data class UpdateQuestDomainEvent(
    val domain: QuestDomain
) : DomainEvent

data class DeleteQuestDomainEvent(
    val domain: QuestDomain
) : DomainEvent

data class CreateQuestMapDomainEvent(
    val domain: QuestMapDomain
) : DomainEvent

data class UpdateQuestMapDomainEvent(
    val domain: QuestMapDomain
) : DomainEvent

data class DeleteQuestMapDomainEvent(
    val domain: QuestMapDomain
) : DomainEvent

interface DomainEvent

data class Projection<T : Domain>(
    val domain: T?,
    val type: EventTypeEnum
)

@Suppress("UNCHECKED_CAST")
fun Projection<*>.toDomainEvent(): DomainEvent =
    when (domain) {
        is QuestMapDomain -> domain.toEvent(this.type)
        is QuestDomain -> domain.toEvent(this.type)
        else -> throw IllegalStateException("Could not convert projection to domain event")
    }

fun QuestDomain.toEvent(event: EventTypeEnum): DomainEvent =
    when (event) {
        EventTypeEnum.CREATE -> CreateQuestDomainEvent(this)
        EventTypeEnum.UPDATE -> UpdateQuestDomainEvent(this)
        EventTypeEnum.DELETE -> DeleteQuestDomainEvent(this)
    }

fun QuestMapDomain.toEvent(event: EventTypeEnum): DomainEvent =
    when (event) {
        EventTypeEnum.CREATE -> CreateQuestMapDomainEvent(this)
        EventTypeEnum.UPDATE -> UpdateQuestMapDomainEvent(this)
        EventTypeEnum.DELETE -> DeleteQuestMapDomainEvent(this)
    }
