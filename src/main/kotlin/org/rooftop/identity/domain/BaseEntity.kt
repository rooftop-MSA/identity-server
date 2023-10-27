package org.rooftop.identity.domain

import jakarta.persistence.PostLoad
import jakarta.persistence.PostPersist
import jakarta.persistence.PreUpdate
import org.springframework.data.domain.Persistable
import java.time.Instant


internal abstract class BaseEntity(
    val createdAt: Instant = Instant.now(),
    var modifiedAt: Instant = Instant.now(),
) : Persistable<Long> {

    @Transient
    private var isNew = true

    abstract override fun getId(): Long?

    override fun isNew(): Boolean = isNew

    @PostLoad
    @PostPersist
    protected open fun updateOldEntity() {
        isNew = false
    }

    @PreUpdate
    fun modifiedAt() {
        modifiedAt = Instant.now()
    }

}
