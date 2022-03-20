package com.ribbontek.template.model.validator

import com.ribbontek.template.model.CreateMapCommand
import com.ribbontek.template.repository.MapRepository
import org.springframework.stereotype.Component
import javax.inject.Inject
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

@Component
class CreateMapCommandValidator : ConstraintValidator<ValidCreateMapCommand, CreateMapCommand>,
    UniqueConstraintValidator() {

    override val message: String = "Must be unique by mapId and name"

    @Inject
    private lateinit var mapRepository: MapRepository

    override fun isValid(value: CreateMapCommand?, context: ConstraintValidatorContext): Boolean {
        return value?.let { command ->
            mapRepository.existsByMapIdOrName(command.mapId, command.name).takeIf { it }?.let {
                context.addConstraintViolationMessage()
                false
            }
        } ?: true
    }
}
