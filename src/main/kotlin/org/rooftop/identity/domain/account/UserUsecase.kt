package org.rooftop.identity.domain.account

import org.rooftop.identity.domain.account.request.UserCreateRequest
import org.rooftop.identity.domain.account.request.UserLoginRequest
import org.rooftop.identity.domain.account.request.UserUpdateRequest
import org.rooftop.identity.domain.account.response.UserResponse
import reactor.core.publisher.Mono

internal interface UserUsecase {

    fun getByName(name: String): Mono<UserResponse>
    fun login(request: UserLoginRequest): Mono<String>

    fun createUser(request: UserCreateRequest): Mono<Unit>

    fun updateUser(request: UserUpdateRequest): Mono<Unit>

    fun deleteUser(id: Long, password: String): Mono<Unit>

}
