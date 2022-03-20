package com.ribbontek.template.util

import com.github.javafaker.Faker
import org.apache.commons.lang3.RandomUtils

object FakerUtil {
    private val faker = Faker()

    fun alphaNumeric(maxSize: Int): String = faker.lorem().characters(maxSize)
    fun randomNumber(minNumber: Long, maxNumber: Long) = faker.number().numberBetween(minNumber, maxNumber)
    inline fun <reified T : Enum<*>> enum(): T =
        T::class.java.enumConstants[RandomUtils.nextInt(0, T::class.java.enumConstants.size)]
}
