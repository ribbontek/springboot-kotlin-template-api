package com.ribbontek.template.repository.model

import com.ribbontek.template.model.domain.QuestMapDomain
import org.hibernate.annotations.SQLDelete
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "vw_map", schema = "ribbontek")
@SQLDelete(sql = "update ribbontek.vw_map set deleted = true where _id = ? and version = ?")
data class MapEntity(

    @Column(name = "map_id", updatable = false)
    val mapId: UUID,

    @Column(name = "name", nullable = false)
    var name: String,

    @Column(name = "url_location")
    var urlLocation: String? = null

) : AbstractEntity() {

    fun update(event: QuestMapDomain) = apply {
        name = event.name
        urlLocation = event.urlLocation
    }
}
