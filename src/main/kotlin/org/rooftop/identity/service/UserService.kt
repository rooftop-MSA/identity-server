package org.rooftop.identity.service

import org.rooftop.identity.domain.*
import org.rooftop.identity.domain.request.UserCreateRequest
import org.rooftop.identity.domain.request.UserDeleteRequest
import org.rooftop.identity.domain.request.UserUpdateRequest
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

    override fun login(name: String, password: String): String {
        val user = getByName(name)

        user.validPassword(password)

        return token.getToken(user.id)
    }

    override fun getByName(name: String): User = userRepository.findByNameOrNull(name)
        ?: throw IllegalArgumentException("Cannot find exist user name \"${name}\"")

    @Transactional
    override fun createUser(request: UserCreateRequest) {
        val newUser = User(idGenerator.generate(), request.name, request.password)
        userRepository.save(newUser)
    }

    @Transactional
    override fun updateUser(request: UserUpdateRequest) {
        val user = getById(request.id)

        user.update(request.newName, request.newPassword)
    }

    @Transactional
    override fun deleteUser(request: UserDeleteRequest) {
        val user = getById(request.id)

        user.validPassword(request.password)

    }

    private fun getById(id: Long): User = userRepository.findByIdOrNull(id)
        ?: throw IllegalArgumentException("Cannot find exist user id \"${id}\"")
}
