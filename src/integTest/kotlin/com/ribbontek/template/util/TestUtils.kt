package com.ribbontek.template.util

import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockHttpServletRequestDsl
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActionsDsl
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

fun MockHttpServletRequestDsl.withJsonEmpty(): MockHttpServletRequestDsl {
    return this.apply {
        this.contentType = MediaType.APPLICATION_JSON
        this.characterEncoding = "utf-8"
    }
}

fun MockHttpServletRequestDsl.withJsonContent(content: Any): MockHttpServletRequestDsl {
    return this.apply {
        this.content = content.toJson()
        this.contentType = MediaType.APPLICATION_JSON
        this.characterEncoding = "utf-8"
    }
}

fun buildMockMvc(
    context: WebApplicationContext,
    builderConsumer: (DefaultMockMvcBuilder) -> Unit = {}
): MockMvc {
    return MockMvcBuilders.webAppContextSetup(context)
        .also(builderConsumer)
        .build()
}

fun ResultActionsDsl.andPrint(): ResultActionsDsl = this.andDo { print() }
fun ResultActionsDsl.andStatusIsOk(): ResultActionsDsl = this.andExpect { status { isOk() } }
fun ResultActionsDsl.andStatusIsNotFound(): ResultActionsDsl = this.andExpect { status { isNotFound() } }
fun ResultActionsDsl.andStatusIsCreated(): ResultActionsDsl = this.andExpect { status { isCreated() } }
