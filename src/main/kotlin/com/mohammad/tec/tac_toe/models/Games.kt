package com.mohammad.tec.tac_toe.models

import org.jetbrains.annotations.NotNull
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.UUID

enum class CellState {
    NONE,
    X,
    O
}

@Table(name = "games")
data class Games(
    @NotNull
    @Id
    val id: UUID?=null,
    @NotNull
    @Column("admin_id")
    val adminId: UUID,
    @Column("created_at")
    val createdAt: LocalDateTime? = null,
    @Column("player_id1")
    var playerId1:UUID?=null,
    @Column("player_id2")
    var playerId2:UUID?=null,
    @Column
    val cell1: CellState=CellState.NONE,
    @Column
    val cell2: CellState=CellState.NONE,
    @Column
    val cell3:CellState=CellState.NONE,
    @Column
    val cell4: CellState=CellState.NONE,
    @Column
    val cell5: CellState=CellState.NONE,
    @Column
    val cell6: CellState=CellState.NONE,
    @Column
    val cell7: CellState=CellState.NONE,
    @Column
    val cell8: CellState=CellState.NONE,
    @Column
    val cell9: CellState=CellState.NONE,
)