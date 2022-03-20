package com.ribbontek.template.repository.event

import com.ribbontek.template.repository.type.PostgreSQLEnumType
import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import org.hibernate.annotations.Generated
import org.hibernate.annotations.GenerationTime
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import java.io.Serializable
import java.time.ZonedDateTime
import java.util.UUID
import javax.persistence.Basic
import javax.persistence.Column
import javax.persistence.DiscriminatorColumn
import javax.persistence.DiscriminatorType
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@MappedSuperclass
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, name = "entity_type")
@TypeDefs(
    value = [
        TypeDef(name = "jsonb", typeClass = JsonBinaryType::class),
        TypeDef(name = "event_type_enum", typeClass = PostgreSQLEnumType::class)
    ]
)
abstract class AbstractEventStoreEntity<T : Any> : Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Generated(value = GenerationTime.INSERT)
    @Column(name = "_id", insertable = false, updatable = false, nullable = false)
    val id: Long? = null

    @get: [Basic(optional = false) Column(updatable = false, nullable = false, name = "entity_type")]
    abstract val entityType: String

    @Basic(optional = false)
    @Column(updatable = false, nullable = false, name = "entity_uuid")
    var entityUUID: UUID? = null

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var old: T? = null

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var new: T? = null

    @Basic(optional = false)
    @Column(updatable = false, nullable = false, name = "event_type")
    @Enumerated(EnumType.STRING)
    @Type(type = "event_type_enum")
    var eventType: EventTypeEnum? = null

    @Generated(value = GenerationTime.INSERT)
    @Basic(optional = false)
    @Column(updatable = false, nullable = false, name = "created_utc")
    var createdUtc: ZonedDateTime? = null
}

enum class EventTypeEnum {
    CREATE, UPDATE, DELETE
}
