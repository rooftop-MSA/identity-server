package org.rooftop.identity.domain

import org.rooftop.identity.domain.request.UserCreateRequest
import org.rooftop.identity.domain.request.UserDeleteRequest
import org.rooftop.identity.domain.request.UserUpdateRequest

internal interface UserUsecase {

    fun getByName(name: String): User
    fun login(name: String, password: String): String

    fun createUser(request: UserCreateRequest)

    fun updateUser(request: UserUpdateRequest)

    fun deleteUser(request: UserDeleteRequest)

}
