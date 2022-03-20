package com.ribbontek.template.repository.type

import org.hibernate.HibernateException
import org.hibernate.engine.spi.SharedSessionContractImplementor
import org.hibernate.type.EnumType
import java.sql.PreparedStatement
import java.sql.SQLException
import java.sql.Types

class PostgreSQLEnumType : EnumType<Enum<*>>() {

    @Throws(HibernateException::class, SQLException::class)
    override fun nullSafeSet(
        st: PreparedStatement,
        value: Any?,
        index: Int,
        session: SharedSessionContractImplementor
    ) {
        value?.let {
            st.setObject(
                index,
                it.toString(),
                Types.OTHER
            )
        } ?: st.setNull(index, Types.OTHER)
    }
}
