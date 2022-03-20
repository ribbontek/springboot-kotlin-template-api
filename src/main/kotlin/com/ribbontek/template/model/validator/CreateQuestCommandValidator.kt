package com.ribbontek.template.model.validator

import com.ribbontek.template.model.CreateQuestCommand
import com.ribbontek.template.repository.QuestRepository
import org.springframework.stereotype.Component
import javax.inject.Inject
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

@Component
class CreateQuestCommandValidator : ConstraintValidator<ValidCreateQuestCommand, CreateQuestCommand>,
    UniqueConstraintValidator() {

    override val message: String = "Must be unique by questId and name"

    @Inject
    private lateinit var questRepository: QuestRepository

    override fun isValid(value: CreateQuestCommand?, context: ConstraintValidatorContext): Boolean {
        return value?.let { command ->
            questRepository.existsByQuestIdOrName(command.questId, command.name).takeIf { it }?.let {
                context.addConstraintViolationMessage()
                false
            }
        } ?: true
    }
}
