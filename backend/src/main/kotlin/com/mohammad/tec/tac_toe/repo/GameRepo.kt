package com.mohammad.tec.tac_toe.repo

import com.mohammad.tec.tac_toe.models.Games
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.*


@Repository
interface GameRepo:CoroutineCrudRepository<Games,UUID> {
    suspend fun findGamesByPlayerId1(id:UUID):Collection<Games>
    suspend fun findGamesByPlayerId2(id:UUID):Collection<Games>
    @Query("SELECT * FROM games where player_id1 = $1 or player_id2 = $1")
    suspend fun findGamesByPlayerId1OrByPlayerId2(id:UUID):Collection<Games>
}