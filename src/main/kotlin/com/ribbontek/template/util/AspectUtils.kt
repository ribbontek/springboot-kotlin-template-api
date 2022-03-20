package com.ribbontek.template.util

import org.aspectj.lang.ProceedingJoinPoint

fun ProceedingJoinPoint.toFunctionName(): String = "${signature.declaringTypeName}::${signature.name}"
