package com.ribbontek.template.handler

import com.ribbontek.template.model.domain.Domain
import com.ribbontek.template.model.domain.Projection

interface CommandHandler<T, R : Domain> {
    fun handle(command: T): Projection<R>
}
