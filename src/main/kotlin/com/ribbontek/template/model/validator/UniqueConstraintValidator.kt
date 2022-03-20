package com.ribbontek.template.model.validator

import javax.validation.ConstraintValidatorContext

abstract class UniqueConstraintValidator {
    protected abstract val message: String
    protected fun ConstraintValidatorContext.addConstraintViolationMessage() {
        disableDefaultConstraintViolation()
        buildConstraintViolationWithTemplate(message).addConstraintViolation()
    }
}
