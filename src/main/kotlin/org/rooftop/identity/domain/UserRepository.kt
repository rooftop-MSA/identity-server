package org.rooftop.identity.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

internal interface UserRepository : JpaRepository<User, Long> {

    @Query("select u from User as u where u.name = :name")
    fun findByNameOrNull(@Param("name") name: String): User?
}
