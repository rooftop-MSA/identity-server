package org.rooftop.identity.domain.request

internal data class UserLoginRequest(
    val name: String,
    val password: String,
)
