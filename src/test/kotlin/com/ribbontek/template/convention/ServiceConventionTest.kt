package com.ribbontek.template.convention

import org.apache.commons.lang3.StringUtils
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.reflections.ReflectionUtils
import org.reflections.Reflections
import org.reflections.scanners.Scanners
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.reflect.Modifier
import kotlin.reflect.KVisibility
import kotlin.reflect.jvm.kotlinProperty

internal class ServiceConventionTest {

    private val log = com.ribbontek.template.util.logger<ServiceConventionTest>()
    private val packageName = "com.ribbontek.template"
    private val passingGrade = 0.85

    @Test
    fun `test all services follow coding standards`() {
        Reflections(
            packageName,
            Scanners.TypesAnnotated
        ).getTypesAnnotatedWith(Service::class.java)
            .forEach { clazz ->
                val foundInterface = clazz.interfaces.firstOrNull {
                    val distance = StringUtils.getJaroWinklerDistance(clazz.simpleName, it.simpleName)
                    log.info("Testing naming similarity distance between ${clazz.simpleName} and ${it.simpleName}. Found $distance")
                    distance >= passingGrade || clazz.simpleName.endsWith(it.simpleName) // ends with case handles CommandHandler / EventProjector
                }
                assertNotNull(foundInterface) { "${clazz.simpleName} should extend an interface with similar name, but received ${foundInterface?.simpleName}" }

                ReflectionUtils.getAllFields(clazz).forEach {
                    // Note: This doesn't catch all scenarios - i.e. API Client
                    log.info("Testing class ${clazz.simpleName} field ${it.name} that it is private")
                    assertTrue(it.kotlinProperty?.visibility == KVisibility.PRIVATE) {
                        "Class ${clazz.simpleName} field ${it.name} should only have private variables"
                    }
                }

                val interfaceMethodNames = ReflectionUtils.getMethods(foundInterface!!).map { it.name }
                val clazzMethods = ReflectionUtils.getMethods(clazz)
                clazzMethods.filter { Modifier.isPublic(it.modifiers) }.forEach {
                    log.info("Testing class ${clazz.simpleName} method ${it.name} that it extends interface method")
                    assertTrue(it.name in interfaceMethodNames) { "Class ${clazz.simpleName} method ${it.name} should be private or else declared in ${foundInterface.simpleName}" }
                    if (clazz.simpleName.contains("Service", true)) {
                        log.info("Testing class ${clazz.simpleName} method ${it.name} that it is annotated with @Transactional annotation with readonly true")
                        assertTrue(it.isAnnotationPresent(Transactional::class.java) && it.getAnnotation(Transactional::class.java).readOnly) {
                            "Class ${clazz.simpleName} method ${it.name} should be annotated with @Transactional annotation with readonly true"
                        }
                    }
                }
            }
    }
}
