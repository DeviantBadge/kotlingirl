package com.kotlingirl.registry.repositories

import com.kotlingirl.registry.model.SessionData
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource(collectionResourceRel = "SessionData", path = "SessionData")
interface SessionDataRepository: JpaRepository<SessionData, Long> {
}