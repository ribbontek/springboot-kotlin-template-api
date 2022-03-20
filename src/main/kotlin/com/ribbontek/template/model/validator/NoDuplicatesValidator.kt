package com.ribbontek.template.model.validator

import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class NoDuplicatesValidator : ConstraintValidator<NoDuplicates, Collection<Any>> {

    override fun isValid(value: Collection<Any>?, context: ConstraintValidatorContext): Boolean {
        return value?.mapIndexed { index, i -> i to index }
            ?.groupBy { it.first }
            ?.filterValues { it.size > 1 }
            ?.keys
            ?.takeIf { it.isNotEmpty() }
            ?.let {
                context.disableDefaultConstraintViolation()
                context.buildConstraintViolationWithTemplate("Found duplicates [${it.joinToString()}]")
                    .addConstraintViolation()
                false
            } ?: true
    }
}
