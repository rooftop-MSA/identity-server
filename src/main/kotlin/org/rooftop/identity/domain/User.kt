package org.rooftop.identity.domain

import jakarta.persistence.*

@Entity
@Table(name = "users", indexes = [Index(name = "idx_users_name", columnList = "name")])
internal class User(
    @Id
    @Column(name = "id")
    val id: Long,
    @Column(name = "name", length = 20, unique = true)
    val name: String,
    @Column(name = "password", length = 255)
    private var password: String,
    @Version
    private var version: Int? = null,
) : BaseEntity() {

    init {
        require(name.length in MIN_NAME_LENGTH..MAX_NAME_LENGTH)
        { "Invalid name length \"${name.length}\"" }
        require(password.length in MIN_PASSWORD_LENGTH..MAX_PASSWORD_LENGTH)
        { "Invalid password length \"${password.length}\"" }
    }

    override fun getId(): Long? {
        return id
    }

    fun validPassword(password: String) {
        require(this.password == password) { "Invalid password" }
    }

    fun updatePassword(newPassword: String) {
        require(password.length in MIN_PASSWORD_LENGTH..MAX_PASSWORD_LENGTH)
        { "Invalid password length \"${password.length}\"" }
        password = newPassword
    }

    private companion object {
        private const val MIN_PASSWORD_LENGTH = 10
        private const val MAX_PASSWORD_LENGTH = 255
        private const val MIN_NAME_LENGTH = 6
        private const val MAX_NAME_LENGTH = 20
    }
}
