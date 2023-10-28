package org.rooftop.identity.domain.request

internal data class UserUpdateRequest(
    val id: Long,
    val newName: String,
    val newPassword: String,
)
