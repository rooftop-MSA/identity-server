package org.rooftop.identity.domain.request

internal data class UserDeleteRequest(
    val id: Long,
    val password: String,
)
