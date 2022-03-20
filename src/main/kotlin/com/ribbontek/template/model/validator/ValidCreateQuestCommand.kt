package com.ribbontek.template.model.validator

import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention
@Constraint(validatedBy = [CreateQuestCommandValidator::class])
annotation class ValidCreateQuestCommand(
    val message: String = "",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
