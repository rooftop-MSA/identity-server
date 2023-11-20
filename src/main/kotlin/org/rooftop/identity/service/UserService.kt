package org.rooftop.identity.service

import org.rooftop.identity.domain.*
import org.rooftop.identity.domain.request.UserCreateRequest
import org.rooftop.identity.domain.request.UserLoginRequest
import org.rooftop.identity.domain.request.UserUpdateRequest
import org.rooftop.identity.domain.response.UserResponse
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
internal class UserService(
    private val userRepository: UserRepository,
    private val idGenerator: IdGenerator,
    private val token: Token,
) : UserUsecase {

    override fun login(request: UserLoginRequest): String {
        val user = userRepository.findByNameOrNull(request.name)
            ?: throw IllegalArgumentException("Cannot find exist user name \"${request.name}\"")
        return getToken(user, request)
    }

    private fun getToken(user: User, request: UserLoginRequest): String {
        user.validPassword(request.password)
        return token.getToken(user.id)
    }

    override fun getByName(name: String): UserResponse {
        val user = userRepository.findByNameOrNull(name)
            ?: throw IllegalArgumentException("Cannot find exist user name \"$name\"")

        return UserResponse(user.id, user.getName())
    }

    @Transactional
    override fun createUser(request: UserCreateRequest) {
        val user = userRepository.findByNameOrNull(request.name)

        if (user != null) {
            throw IllegalArgumentException("Duplicated user name \"${request.name}\"")
        }

        userRepository.save(
            User(
                idGenerator.generate(),
                request.name,
                request.userName,
                request.password
            )
        )
    }

    @Transactional
    override fun updateUser(request: UserUpdateRequest) {
        var user = getById(request.id)
        updateUser(user, request)
    }

    private fun updateUser(user: User, request: UserUpdateRequest): User {
        return user.run {
            this.update(request.newName, request.newUserName, request.newPassword)
            userRepository.save(this)
        }
    }

    @Transactional
    override fun deleteUser(id: Long, password: String) {
        val user = getById(id)
        deleteUser(user, password)
    }

    private fun deleteUser(user: User, password: String) {
        user.validPassword(password)
        return userRepository.delete(user)
    }

    private fun getById(id: Long): User =
        userRepository.findByIdOrNull(id)
            ?: throw IllegalArgumentException("Cannot find exist user id \"${id}\"")
}
