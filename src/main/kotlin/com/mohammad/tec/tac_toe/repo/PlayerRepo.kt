package com.mohammad.tec.tac_toe.repo

import com.mohammad.tec.tac_toe.models.Players
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.*

interface PlayerRepo: CoroutineCrudRepository<Players, UUID> {

   suspend fun findByIpAddress(ip:String): Players?
}