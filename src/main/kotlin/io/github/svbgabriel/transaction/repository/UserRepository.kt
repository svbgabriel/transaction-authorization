package io.github.svbgabriel.transaction.repository

import io.github.svbgabriel.transaction.repository.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, Long>
