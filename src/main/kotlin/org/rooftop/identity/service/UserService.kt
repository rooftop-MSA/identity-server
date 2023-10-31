package org.rooftop.identity.service

import org.rooftop.identity.domain.*
import org.rooftop.identity.domain.request.UserCreateRequest
import org.rooftop.identity.domain.request.UserDeleteRequest
import org.rooftop.identity.domain.request.UserLoginRequest
import org.rooftop.identity.domain.request.UserUpdateRequest
import org.rooftop.identity.domain.response.UserResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono

@Service
@Transactional(readOnly = true)
internal class UserService(
    private val userRepository: UserRepository,
    private val idGenerator: IdGenerator,
    private val token: Token,
) : UserUsecase {

    override fun login(request: UserLoginRequest): Mono<String> {
        return userRepository.findByName(request.name)
            .doOnError { throw IllegalArgumentException("Cannot find exist user name \"${request.name}\"") }
            .doOnNext { user -> user.validPassword(request.password) }
            .map { user -> token.getToken(user.id) }
    }

    override fun getByName(name: String): Mono<UserResponse> {
        return userRepository.findByName(name)
            .doOnError { throw IllegalArgumentException("Cannot find exist user name \"${name}\"") }
            .map { user -> UserResponse(user.id, user.getName()) }
    }

    @Transactional
    override fun createUser(request: UserCreateRequest): Mono<Unit> {
        return userRepository.findByName(request.name)
            .hasElement()
            .map { throw IllegalArgumentException("Duplicated user name \"${request.name}\"") }
            .doOnNext {
                userRepository.save(
                    User(
                        idGenerator.generate(),
                        request.name,
                        request.password,
                        isNew = true
                    )
                )
            }.map { }
    }

    @Transactional
    override fun updateUser(request: UserUpdateRequest): Mono<Unit> {
        return getById(request.id)
            .doOnNext { user -> user.update(request.newName, request.newPassword) }
            .map { }
    }

    @Transactional
    override fun deleteUser(request: UserDeleteRequest): Mono<Unit> {
        return getById(request.id)
            .doOnNext { user -> user.validPassword(request.password) }
            .doOnNext { user -> userRepository.delete(user) }
            .map { }
    }

    private fun getById(id: Long): Mono<User> = userRepository.findById(id)
        .doOnError { throw IllegalArgumentException("Cannot find exist user id \"${id}\"") }
}
