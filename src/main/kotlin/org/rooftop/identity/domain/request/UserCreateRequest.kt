package org.rooftop.identity.domain.request

internal data class UserCreateRequest(
    val name: String,
    val password: String,
)
