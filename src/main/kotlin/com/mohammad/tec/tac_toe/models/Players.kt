package com.mohammad.tec.tac_toe.models

import org.jetbrains.annotations.NotNull
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.UUID


@Table(name = "players")
data class Players(
    @NotNull
    @Id
    val id: UUID? = null,
    @Column
    val name: String,
    @Column("ip_address")
    val ipAddress: String,
    @Column("is_active")
    var isActive: Boolean
)