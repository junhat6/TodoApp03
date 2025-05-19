package com.example.todo.domain.model

import java.util.UUID

/**
 * TodoエンティティのID
 * DDDにおける値オブジェクト
 */
data class TodoId(val value: UUID) {
    override fun toString(): String = value.toString()
} 