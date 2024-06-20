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
    @Column("player_id_turn")
    var playerIdTurn:UUID?=null,
    @Column("current_cell_type")
    var currentCellType:CellState=CellState.X,
    @Column
    var cell1: CellState=CellState.NONE,
    @Column
    var cell2: CellState=CellState.NONE,
    @Column
    var cell3:CellState=CellState.NONE,
    @Column
    var cell4: CellState=CellState.NONE,
    @Column
    var cell5: CellState=CellState.NONE,
    @Column
    var cell6: CellState=CellState.NONE,
    @Column
    var cell7: CellState=CellState.NONE,
    @Column
    var cell8: CellState=CellState.NONE,
    @Column
    var cell9: CellState=CellState.NONE,
)