package org.rooftop.identity.domain

import org.springframework.data.jpa.repository.JpaRepository

internal interface UserRepository : JpaRepository<User, Long>
