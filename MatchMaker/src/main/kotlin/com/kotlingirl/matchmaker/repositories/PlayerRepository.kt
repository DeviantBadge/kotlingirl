package com.kotlingirl.matchmaker.repositories

import com.kotlingirl.matchmaker.model.Player
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import java.util.*


@RepositoryRestResource(collectionResourceRel = "users", path = "users")
interface PlayerRepository : JpaRepository<Player, Long> {
    fun findByLogin(name: String): Player
}