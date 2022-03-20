package com.ribbontek.template.projector

import com.ribbontek.template.model.domain.DomainEvent

interface EventProjector<T : DomainEvent> {
    fun project(event: T)
}
