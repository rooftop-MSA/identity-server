package org.rooftop.identity.service

import org.rooftop.identity.domain.IdGenerator
import org.rooftop.identity.domain.account.User
import org.rooftop.identity.domain.account.UserRepository
import org.rooftop.identity.domain.account.UserUsecase
import org.rooftop.identity.domain.account.event.UserCreatedEvent
import org.rooftop.identity.domain.account.request.UserCreateRequest
import org.rooftop.identity.domain.account.request.UserLoginRequest
import org.rooftop.identity.domain.account.request.UserUpdateRequest
import org.rooftop.identity.domain.account.response.UserResponse
import org.rooftop.identity.domain.identity.Token
import org.springframework.context.ApplicationEventPublisher
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
    private val eventPublisher: ApplicationEventPublisher,
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

    override fun getById(id: Long): Mono<UserResponse> {
        return getUserById(id)
            .map { UserResponse(it.id, it.getName()) }
    }

    @Transactional
    override fun createUser(request: UserCreateRequest): Mono<Unit> {
        return userRepository.save(
            User(
                idGenerator.generate(),
                request.name,
                request.userName,
                request.password,
                isNew = true
            )
        ).onErrorMap {
            throw IllegalArgumentException("Duplicated user name \"${request.name}\"")
        }.doOnSuccess {
            eventPublisher.publishEvent(UserCreatedEvent(it.id))
        }.map { }
    }

    @Modifying
    @Transactional
    override fun updateUser(request: UserUpdateRequest): Mono<Unit> {
        return getUserById(request.id)
            .flatMap { updateUser(it, request) }
            .map { }
    }

    private fun updateUser(user: User, request: UserUpdateRequest): Mono<User> {
        return user.run {
            this.update(request.newName, request.newUserName, request.newPassword)
            userRepository.save(this)
        }
    }

    @Transactional
    override fun deleteUser(id: Long, password: String): Mono<Unit> {
        return getUserById(id).flatMap { deleteUser(it, password) }
    }

    private fun deleteUser(user: User, password: String): Mono<Unit> {
        user.validPassword(password)
        return userRepository.delete(user)
            .map { }
    }

    override fun getByToken(token: String): Mono<UserResponse> {
        return Mono.fromCallable { this.token.getId(token) }
            .flatMap {
                getUserById(it)
            }
            .map { user ->
                UserResponse(user.id, user.getName())
            }
    }

    private fun getUserById(id: Long): Mono<User> = userRepository.findById(id)
        .switchIfEmpty(Mono.error { throw IllegalArgumentException("Cannot find exist user id \"${id}\"") })
}
