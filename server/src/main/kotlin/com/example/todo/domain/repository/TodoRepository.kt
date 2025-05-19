package com.example.todo.domain.repository

import com.example.todo.domain.model.Todo
import com.example.todo.domain.model.TodoId

/**
 * Todoリポジトリのインターフェース
 * DDDにおけるリポジトリパターン
 */
interface TodoRepository {
    fun findAll(): List<Todo>
    fun findById(id: TodoId): Todo?
    fun save(todo: Todo): Todo
    fun delete(id: TodoId)
    fun deleteAll()
    fun deleteByCategory(category: String)
} 