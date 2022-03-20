package com.ribbontek.template.model.validator

import com.ribbontek.template.repository.MapRepository
import org.springframework.stereotype.Component
import java.util.UUID
import javax.inject.Inject
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

@Component
class MapIdListValidator : ConstraintValidator<ValidMapIdList, List<UUID>> {

    @Inject
    private lateinit var mapRepository: MapRepository

    override fun isValid(value: List<UUID>?, context: ConstraintValidatorContext): Boolean {
        return value?.map { it to mapRepository.existsByMapId(it) }
            ?.filter { !it.second }
            ?.takeIf { it.isNotEmpty() }
            ?.let { idToExists ->
                context.disableDefaultConstraintViolation()
                context.buildConstraintViolationWithTemplate("Found invalid map ids [${idToExists.map { it.first }.joinToString()}]")
                    .addConstraintViolation()
                false
            } ?: true
    }
}
