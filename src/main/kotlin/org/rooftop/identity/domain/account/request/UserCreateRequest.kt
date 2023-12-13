package org.rooftop.identity.domain.account.request

internal data class UserCreateRequest(
    val name: String,
    val userName: String,
    val password: String,
)
