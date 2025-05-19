package com.example.todo.infrastructure.entity

import com.example.todo.domain.model.Todo
import com.example.todo.domain.model.TodoId
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

/**
 * TodoエンティティのJPAマッピングクラス
 */
@Entity
@Table(name = "todos")
class TodoEntity(
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    val id: UUID,

    @Column(name = "text", nullable = false, length = 100)
    var text: String,

    @Column(name = "category", nullable = false, length = 50)
    var category: String,

    @Column(name = "completed", nullable = false)
    var completed: Boolean,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime,

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime
) {
    companion object {
        fun fromDomain(todo: Todo): TodoEntity {
            return TodoEntity(
                id = todo.id.value,
                text = todo.getText(),
                category = todo.getCategory(),
                completed = todo.isCompleted(),
                createdAt = todo.getCreatedAt(),
                updatedAt = todo.getUpdatedAt()
            )
        }
    }

    fun toDomain(): Todo {
        return TodoEntityMapper.toDomain(this)
    }
}

/**
 * TodoEntityからTodoドメインオブジェクトへの変換を行うマッパー
 */
object TodoEntityMapper {
    fun toDomain(entity: TodoEntity): Todo {
        val todoClass = Todo::class.java
        val constructor = todoClass.getDeclaredConstructor(
            TodoId::class.java,
            String::class.java,
            String::class.java,
            Boolean::class.java,
            LocalDateTime::class.java,
            LocalDateTime::class.java
        )
        constructor.isAccessible = true
        
        val todo = constructor.newInstance(
            TodoId(entity.id),
            entity.text,
            entity.category,
            entity.completed,
            entity.createdAt,
            entity.updatedAt
        )
        
        constructor.isAccessible = false
        return todo
    }
} 