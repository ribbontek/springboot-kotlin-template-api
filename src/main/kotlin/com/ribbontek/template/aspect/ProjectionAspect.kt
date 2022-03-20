package com.ribbontek.template.aspect

import com.ribbontek.template.model.domain.Projection
import com.ribbontek.template.model.domain.toDomainEvent
import com.ribbontek.template.util.logger
import com.ribbontek.template.util.toFunctionName
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.Logger
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class Projector

interface ProjectionAspect {
    fun project(joinPoint: ProceedingJoinPoint, projector: Projector): Any?
}

@Aspect
@Component
class ProjectionAspectImpl(
    private val publisher: ApplicationEventPublisher
) : ProjectionAspect {

    private val log: Logger = logger()

    @Around("execution(* *(..)) && @annotation(projector)")
    @Throws(Throwable::class)
    @Suppress("UNCHECKED_CAST")
    override fun project(joinPoint: ProceedingJoinPoint, projector: Projector): Any? {
        try {
            val projection = joinPoint.proceed()
            assert(projection is Projection<*>) { "Expected ${joinPoint.toFunctionName()} to return a Projection" }
            (projection as Projection<*>).run {
                log.info("Publishing new $type event for ${domain?.let { it::class.simpleName } ?: "unknown"}")
                publisher.publishEvent(this.toDomainEvent())
            }
            return projection
        } catch (ex: Exception) {
            throw ex
        }
    }
}
