package com.example.todo.application.service

import com.example.todo.domain.model.Todo
import com.example.todo.domain.model.TodoId
import com.example.todo.domain.repository.TodoRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.UUID

/**
 * Todoアプリケーションサービス
 * DDDにおけるアプリケーションサービス
 */
@Service
@Transactional
class TodoService(private val todoRepository: TodoRepository) {

    /**
     * 全てのTodoを取得する
     */
    @Transactional(readOnly = true)
    fun getAllTodos(): List<Todo> {
        return todoRepository.findAll()
    }

    /**
     * IDによってTodoを取得する
     */
    @Transactional(readOnly = true)
    fun getTodoById(id: String): Todo? {
        val todoId = TodoId(UUID.fromString(id))
        return todoRepository.findById(todoId)
    }

    /**
     * 新しいTodoを作成する
     */
    fun createTodo(text: String, category: String): Todo {
        val todo = Todo.create(text, category)
        return todoRepository.save(todo)
    }

    /**
     * Todoを更新する
     */
    fun updateTodo(id: String, text: String, category: String): Todo? {
        val todoId = TodoId(UUID.fromString(id))
        val todo = todoRepository.findById(todoId) ?: return null

        todo.updateText(text)
        todo.updateCategory(category)

        return todoRepository.save(todo)
    }

    /**
     * Todoを完了済みにする
     */
    fun markTodoAsCompleted(id: String): Todo? {
        val todoId = TodoId(UUID.fromString(id))
        val todo = todoRepository.findById(todoId) ?: return null

        todo.markAsCompleted()
        return todoRepository.save(todo)
    }

    /**
     * Todoを未完了に戻す
     */
    fun markTodoAsIncomplete(id: String): Todo? {
        val todoId = TodoId(UUID.fromString(id))
        val todo = todoRepository.findById(todoId) ?: return null

        todo.markAsIncomplete()
        return todoRepository.save(todo)
    }

    /**
     * Todoを削除する
     */
    fun deleteTodo(id: String) {
        val todoId = TodoId(UUID.fromString(id))
        todoRepository.delete(todoId)
    }

    /**
     * すべてのTodoを削除する
     */
    fun deleteAllTodos() {
        todoRepository.deleteAll()
    }

    /**
     * カテゴリー別にTodoを削除する
     */
    fun deleteTodosByCategory(category: String) {
        todoRepository.deleteByCategory(category)
    }
} 