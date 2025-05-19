package com.example.todo.infrastructure.repository

import com.example.todo.domain.model.Todo
import com.example.todo.domain.model.TodoId
import com.example.todo.domain.repository.TodoRepository
import com.example.todo.infrastructure.entity.TodoEntity
import org.springframework.stereotype.Repository
import java.util.UUID

/**
 * TodoRepositoryの実装クラス
 */
@Repository
class TodoRepositoryImpl(
    private val jpaRepository: SpringDataJpaTodoRepository
) : TodoRepository {

    override fun findAll(): List<Todo> {
        return jpaRepository.findAll().map { it.toDomain() }
    }

    override fun findById(id: TodoId): Todo? {
        return jpaRepository.findById(id.value).map { it.toDomain() }.orElse(null)
    }

    override fun save(todo: Todo): Todo {
        val entity = TodoEntity.fromDomain(todo)
        return jpaRepository.save(entity).toDomain()
    }

    override fun delete(id: TodoId) {
        jpaRepository.deleteById(id.value)
    }

    override fun deleteAll() {
        jpaRepository.deleteAll()
    }

    override fun deleteByCategory(category: String) {
        jpaRepository.deleteByCategory(category)
    }
} 