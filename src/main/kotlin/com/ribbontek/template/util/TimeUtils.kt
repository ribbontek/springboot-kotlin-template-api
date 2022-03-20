package com.ribbontek.template.util

import java.time.ZoneOffset
import java.time.ZonedDateTime

fun ZonedDateTime.toUtc() = ZonedDateTime.now(ZoneOffset.UTC)
