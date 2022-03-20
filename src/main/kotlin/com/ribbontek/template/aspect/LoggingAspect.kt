package com.ribbontek.template.aspect

import com.ribbontek.template.util.logger
import com.ribbontek.template.util.toFunctionName
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.Logger
import org.springframework.stereotype.Component
import org.springframework.util.StopWatch

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class Logging

interface LoggingAspect {
    fun logging(joinPoint: ProceedingJoinPoint, logging: Logging): Any?
}

@Aspect
@Component
class LoggingAspectImpl : LoggingAspect {

    private val log: Logger = logger()

    @Around("execution(* *(..)) && @annotation(logging)")
    @Throws(Throwable::class)
    override fun logging(joinPoint: ProceedingJoinPoint, logging: Logging): Any? {
        with(StopWatch()) {
            val callingMethod = joinPoint.toFunctionName()
            log.info("$callingMethod --> started execution")
            start(callingMethod)
            return try {
                joinPoint.proceed()
            } catch (ex: Exception) {
                throw ex
            } finally {
                stop()
                log.info("$callingMethod --> finished execution in ${lastTaskTimeMillis}ms")
            }
        }
    }
}
