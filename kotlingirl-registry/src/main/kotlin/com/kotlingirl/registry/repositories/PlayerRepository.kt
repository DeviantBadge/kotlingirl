package com.kotlingirl.registry.repositories

import com.kotlingirl.registry.model.Player
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource


@RepositoryRestResource(collectionResourceRel = "users", path = "users")
interface PlayerRepository : JpaRepository<Player, Long> {
    fun findByLogin(name: String): Player?
    //fun findAllByLogged()
}