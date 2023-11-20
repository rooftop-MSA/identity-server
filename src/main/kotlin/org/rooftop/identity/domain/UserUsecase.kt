package org.rooftop.identity.domain

import org.rooftop.identity.domain.request.UserCreateRequest
import org.rooftop.identity.domain.request.UserLoginRequest
import org.rooftop.identity.domain.request.UserUpdateRequest
import org.rooftop.identity.domain.response.UserResponse

internal interface UserUsecase {

    fun getByName(name: String): UserResponse
    fun login(request: UserLoginRequest): String

    fun createUser(request: UserCreateRequest): Unit

    fun updateUser(request: UserUpdateRequest): Unit

    fun deleteUser(id: Long, password: String): Unit

}
