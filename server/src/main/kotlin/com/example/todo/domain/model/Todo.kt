package com.example.todo.domain.model

import java.time.LocalDateTime
import java.util.UUID

/**
 * Todoエンティティ
 * DDDにおける集約ルート
 */
class Todo private constructor(
    val id: TodoId,
    private var text: String,
    private var category: String,
    private var completed: Boolean,
    private val createdAt: LocalDateTime,
    private var updatedAt: LocalDateTime
) {
    companion object {
        fun create(text: String, category: String): Todo {
            val now = LocalDateTime.now()
            return Todo(
                id = TodoId(UUID.randomUUID()),
                text = text,
                category = category,
                completed = false,
                createdAt = now,
                updatedAt = now
            )
        }
    }

    fun getText(): String = text
    fun getCategory(): String = category
    fun isCompleted(): Boolean = completed
    fun getCreatedAt(): LocalDateTime = createdAt
    fun getUpdatedAt(): LocalDateTime = updatedAt

    fun updateText(text: String) {
        this.text = text
        this.updatedAt = LocalDateTime.now()
    }

    fun updateCategory(category: String) {
        this.category = category
        this.updatedAt = LocalDateTime.now()
    }

    fun markAsCompleted() {
        if (!this.completed) {
            this.completed = true
            this.updatedAt = LocalDateTime.now()
        }
    }

    fun markAsIncomplete() {
        if (this.completed) {
            this.completed = false
            this.updatedAt = LocalDateTime.now()
        }
    }
} 