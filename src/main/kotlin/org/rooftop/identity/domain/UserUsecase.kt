package org.rooftop.identity.domain

import org.rooftop.identity.domain.request.UserCreateRequest
import org.rooftop.identity.domain.request.UserDeleteRequest
import org.rooftop.identity.domain.request.UserLoginRequest
import org.rooftop.identity.domain.request.UserUpdateRequest
import org.rooftop.identity.domain.response.UserResponse
import reactor.core.publisher.Mono

internal interface UserUsecase {

    fun getByName(name: String): Mono<UserResponse>
    fun login(request: UserLoginRequest): Mono<String>

    fun createUser(request: UserCreateRequest): Mono<Unit>

    fun updateUser(request: UserUpdateRequest): Mono<Unit>

    fun deleteUser(request: UserDeleteRequest): Mono<Unit>

}
