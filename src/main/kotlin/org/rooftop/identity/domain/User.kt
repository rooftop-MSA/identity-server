package org.rooftop.identity.domain

import jakarta.persistence.*

@Entity
@Table(name = "users", indexes = [Index(name = "idx_users_name", columnList = "name")])
internal class User(
    @Id
    @Column(name = "id")
    val id: Long,
    @Column(name = "name", length = 20, unique = true)
    private var name: String,
    @Column(name = "password", length = 255)
    private var password: String,
    @Version
    private var version: Int? = null,
) : BaseEntity() {

    init {
        validNameLength(name)
        validPasswordLength(password)
    }

    override fun getId(): Long? {
        return id
    }

    fun validPassword(password: String) {
        require(this.password == password) { "Invalid password" }
    }

    fun update(newName: String?, newPassword: String?) {
        newName?.let {
            validNameLength(it)
            name = it
        }

        newPassword?.let {
            validPasswordLength(it)
            password = it
        }
    }

    private fun validNameLength(name: String) {
        require(name.length in MIN_NAME_LENGTH..MAX_NAME_LENGTH) {
            "Invalid name length \"${name.length}\""
        }
    }

    private fun validPasswordLength(password: String) {
        require(password.length in MIN_PASSWORD_LENGTH..MAX_PASSWORD_LENGTH) {
            "Invalid password length \"${password.length}\""
        }
    }

    fun getName(): String = name

    private companion object {
        private const val MIN_PASSWORD_LENGTH = 10
        private const val MAX_PASSWORD_LENGTH = 255
        private const val MIN_NAME_LENGTH = 6
        private const val MAX_NAME_LENGTH = 20
    }
}
