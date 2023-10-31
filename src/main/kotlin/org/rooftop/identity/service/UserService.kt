package org.rooftop.identity.service

import org.rooftop.identity.domain.*
import org.rooftop.identity.domain.request.UserCreateRequest
import org.rooftop.identity.domain.request.UserLoginRequest
import org.rooftop.identity.domain.request.UserUpdateRequest
import org.rooftop.identity.domain.response.UserResponse
import org.springframework.data.r2dbc.repository.Modifying
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
            .switchIfEmpty(Mono.error { throw IllegalArgumentException("Cannot find exist user name \"${request.name}\"") })
            .flatMap { getToken(it, request) }
    }

    private fun getToken(user: User, request: UserLoginRequest): Mono<String> {
        user.validPassword(request.password)
        return Mono.just(token.getToken(user.id))
    }

    override fun getByName(name: String): Mono<UserResponse> {
        return userRepository.findByName(name)
            .switchIfEmpty(Mono.error { throw IllegalArgumentException("Cannot find exist user name \"$name\"") })
            .map { user -> UserResponse(user.id, user.getName()) }
    }

    @Transactional
    override fun createUser(request: UserCreateRequest): Mono<Unit> {
        return userRepository.findByName(request.name)
            .switchIfEmpty(
                userRepository.save(
                    User(idGenerator.generate(), request.name, request.password, isNew = true)
                )
            )
            .filterWhen { isNotNew(it) }
            .map {
                require(it.isNew) {
                    throw IllegalArgumentException("Duplicated user name \"${request.name}\"")
                }
            }
    }

    private fun isNotNew(user: User): Mono<Boolean> = Mono.just(!user.isNew)

    @Modifying
    @Transactional
    override fun updateUser(request: UserUpdateRequest): Mono<Unit> {
        return getById(request.id)
            .flatMap { updateUser(it, request) }
            .map { }
    }

    private fun updateUser(user: User, request: UserUpdateRequest): Mono<User> {
        return user.run {
            this.update(request.newName, request.newPassword)
            userRepository.save(this)
        }
    }

    @Transactional
    override fun deleteUser(id: Long, password: String): Mono<Unit> {
        return getById(id).flatMap { deleteUser(it, password) }
    }

    private fun deleteUser(user: User, password: String): Mono<Unit> {
        user.validPassword(password)
        return userRepository.delete(user)
            .map { }
    }

    private fun getById(id: Long): Mono<User> = userRepository.findById(id)
        .switchIfEmpty(Mono.error { throw IllegalArgumentException("Cannot find exist user id \"${id}\"") })
}
