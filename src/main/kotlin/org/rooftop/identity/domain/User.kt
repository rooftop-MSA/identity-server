package org.rooftop.identity.domain

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.PersistenceCreator
import org.springframework.data.annotation.Version
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table(name = "users")
internal class User(
    @Id
    @Column("id")
    val id: Long,
    @Column("name")
    private var name: String,
    @Column("user_name")
    private var userName: String,
    @Column("password")
    private var password: String,
    @Version
    private var version: Int? = null,
    isNew: Boolean = false,
) : BaseEntity(isNew) {

    @PersistenceCreator
    constructor(
        id: Long,
        name: String,
        userName: String,
        password: String,
    ) : this(id, name, userName, password, isNew = false)

    init {
        validNameLength(name)
        validUserNameLength(userName)
        validPasswordLength(password)
    }

    override fun getId(): Long {
        return id
    }

    fun validPassword(password: String) {
        require(this.password == password) { "Invalid password" }
    }

    fun update(newName: String?, newUserName: String?, newPassword: String?) {
        newName?.let {
            validNameLength(it)
            name = it
        }

        newUserName?.let {
            validUserNameLength(it)
            userName = it
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

    private fun validUserNameLength(userName: String) {
        require(userName.length in MIN_USER_NAME_LENGTH..MAX_USER_NAME_LENGTH) {
            "Invalid user_name length \"${userName.length}\""
        }
    }

    private fun validPasswordLength(password: String) {
        require(password.length in MIN_PASSWORD_LENGTH..MAX_PASSWORD_LENGTH) {
            "Invalid password length \"${password.length}\""
        }
    }

    internal fun getName(): String = name
    override fun toString(): String {
        return "User(id=$id, name='$name', password='$password', version=$version)"
    }


    private companion object {
        private const val MIN_PASSWORD_LENGTH = 10
        private const val MAX_PASSWORD_LENGTH = 255
        private const val MIN_NAME_LENGTH = 6
        private const val MAX_NAME_LENGTH = 20
        private const val MIN_USER_NAME_LENGTH = 1
        private const val MAX_USER_NAME_LENGTH = 20
    }
}
