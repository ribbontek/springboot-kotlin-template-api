package com.ribbontek.template.repository.model

import org.hibernate.annotations.Generated
import org.hibernate.annotations.GenerationTime
import java.io.Serializable
import java.time.ZonedDateTime
import javax.persistence.Basic
import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.MappedSuperclass
import javax.persistence.Version

@MappedSuperclass
abstract class AbstractEntity : Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Generated(value = GenerationTime.INSERT)
    @Column(name = "_id", insertable = false, updatable = false, nullable = false)
    val id: Long? = null

    @Generated(value = GenerationTime.INSERT)
    @Basic(optional = false)
    @Column(updatable = false, nullable = false, name = "created_utc")
    var createdUtc: ZonedDateTime? = null

    @Column(insertable = false, name = "updated_utc")
    var updatedUtc: ZonedDateTime? = null

    @Generated(value = GenerationTime.INSERT)
    @Column(insertable = false, nullable = false)
    var deleted = false

    @Version
    @Column(nullable = false)
    val version: Int? = null
}
