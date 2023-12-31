package org.rooftop.identity.domain.account

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import java.time.Instant
import java.util.*

internal abstract class BaseEntity(
    @Transient
    private val isNew: Boolean = false,
    @LastModifiedDate
    @Column("created_at")
    var createdAt: Instant? = null,
    @CreatedDate
    @Column("modified_at")
    var modifiedAt: Instant? = null,
) : Persistable<Long> {

    abstract override fun getId(): Long?

    override fun isNew(): Boolean = isNew
}
