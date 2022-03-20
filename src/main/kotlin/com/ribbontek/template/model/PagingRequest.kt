package com.ribbontek.template.model

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import java.io.Serializable

data class PagingRequest<T : Query>(
    val number: Int,
    val size: Int,
    val sorted: Sort? = null,
    val query: T? = null
) : Pageable, Serializable {
    companion object {
        fun <T : Query> of(number: Int, size: Int) = PagingRequest<T>(number, size)
        val DEFAULT: PagingRequest<Query> = of(0, 10)
    }

    override fun getPageNumber(): Int = number
    override fun getSort(): Sort = sorted ?: Sort.unsorted()
    override fun getPageSize(): Int = size
    override fun getOffset(): Long = number.toLong() * size.toLong()
    override fun hasPrevious(): Boolean = number > 0

    @JsonIgnore
    override fun next(): Pageable = PagingRequest<Query>(pageNumber + 1, pageSize, getSort())

    @JsonIgnore
    override fun first(): Pageable = PagingRequest<Query>(0, pageSize, sorted)

    @JsonIgnore
    override fun withPage(pageNumber: Int): Pageable = PagingRequest<Query>(pageNumber, pageSize, getSort())

    @JsonIgnore
    private fun previous(): Pageable = if (pageNumber == 0) this else PagingRequest<Query>(pageNumber - 1, pageSize, sorted)

    @JsonIgnore
    override fun previousOrFirst(): Pageable = if (hasPrevious()) previous() else first()
}
