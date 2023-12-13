package org.rooftop.identity.domain.account.request

internal data class UserUpdateRequest(
    val id: Long,
    val newName: String?,
    val newPassword: String?,
    val newUserName: String?,
)
