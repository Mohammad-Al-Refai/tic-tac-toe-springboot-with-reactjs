package com.mohammad.tec.tac_toe.repo

import com.mohammad.tec.tac_toe.models.Games
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.*


@Repository
interface GameRepo:CoroutineCrudRepository<Games,UUID> {
}