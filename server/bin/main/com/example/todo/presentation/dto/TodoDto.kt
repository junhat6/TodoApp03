package com.example.todo.presentation.dto

import com.example.todo.domain.model.Todo
import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

/**
 * TodoのDTO
 */
data class TodoResponse(
    val id: String,
    val text: String,
    val category: String,
    val completed: Boolean,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val createdAt: LocalDateTime,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val updatedAt: LocalDateTime
) {
    companion object {
        fun fromDomain(todo: Todo): TodoResponse {
            return TodoResponse(
                id = todo.id.toString(),
                text = todo.getText(),
                category = todo.getCategory(),
                completed = todo.isCompleted(),
                createdAt = todo.getCreatedAt(),
                updatedAt = todo.getUpdatedAt()
            )
        }
    }
}

/**
 * Todoの作成リクエスト
 */
data class CreateTodoRequest(
    @field:NotBlank(message = "テキストは必須です")
    @field:Size(max = 100, message = "テキストは100文字以内で入力してください")
    val text: String,

    val category: String = "個人"
)

/**
 * Todoの更新リクエスト
 */
data class UpdateTodoRequest(
    @field:NotBlank(message = "テキストは必須です")
    @field:Size(max = 100, message = "テキストは100文字以内で入力してください")
    val text: String,

    val category: String = "個人"
) 