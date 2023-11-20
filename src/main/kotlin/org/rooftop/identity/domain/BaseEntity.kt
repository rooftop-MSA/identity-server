package org.rooftop.identity.domain

import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.Transient
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.domain.Persistable
import java.time.Instant

@MappedSuperclass
internal abstract class BaseEntity(
    @Transient
    private val isNew: Boolean = false,
    @LastModifiedDate
    @Column(name = "created_at")
    var createdAt: Instant? = Instant.now(),
    @CreatedDate
    @Column(name = "modified_at")
    var modifiedAt: Instant? = Instant.now(),
) : Persistable<Long> {

    abstract override fun getId(): Long?

    override fun isNew(): Boolean = isNew
}
