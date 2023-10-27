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

    override fun getId(): Long? {
        return id
    }

    fun validPassword(password: String) {
        require(this.password == password) { "Invalid password" }
    }

    fun updatePassword(newPassword: String) {
        password = newPassword
    }
}
