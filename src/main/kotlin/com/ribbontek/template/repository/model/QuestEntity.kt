package com.ribbontek.template.repository.model

import com.ribbontek.template.model.QuestLevel
import com.ribbontek.template.model.QuestStatus
import com.ribbontek.template.model.domain.QuestDomain
import com.ribbontek.template.repository.type.PostgreSQLEnumType
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import java.util.UUID
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.Table

@Entity
@Table(name = "vw_quest", schema = "ribbontek")
@TypeDefs(
    value = [
        TypeDef(name = "quest_level_enum", typeClass = PostgreSQLEnumType::class),
        TypeDef(name = "quest_status_enum", typeClass = PostgreSQLEnumType::class)
    ]
)
@SQLDelete(sql = "update ribbontek.vw_quest set deleted = true where _id = ? and version = ?")
data class QuestEntity(

    @Column(name = "quest_id", updatable = false)
    val questId: UUID,

    @Column(name = "name", nullable = false)
    var name: String,

    @Column(name = "description")
    var description: String? = null,

    @Column(name = "level")
    @Type(type = "quest_level_enum")
    @Enumerated(EnumType.STRING)
    var level: QuestLevel,

    @Column(name = "status", nullable = false)
    @Type(type = "quest_status_enum")
    @Enumerated(EnumType.STRING)
    var status: QuestStatus,

    @ManyToMany(cascade = [CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH])
    @JoinTable(
        schema = "ribbontek",
        name = "quest_to_map",
        joinColumns = [JoinColumn(name = "quest_id", referencedColumnName = "quest_id")],
        inverseJoinColumns = [JoinColumn(name = "map_id", referencedColumnName = "map_id")]
    )
    var maps: MutableSet<MapEntity>? = null

) : AbstractEntity() {

    fun update(event: QuestDomain, newMaps: MutableSet<MapEntity>?) = apply {
        name = event.name
        description = event.description
        level = event.level
        status = event.status
        maps = newMaps
    }
}
