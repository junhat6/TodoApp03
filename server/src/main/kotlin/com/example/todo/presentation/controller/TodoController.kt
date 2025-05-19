package com.example.todo.presentation.controller

import com.example.todo.application.service.TodoService
import com.example.todo.presentation.dto.CreateTodoRequest
import com.example.todo.presentation.dto.TodoResponse
import com.example.todo.presentation.dto.UpdateTodoRequest
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

/**
 * Todoのコントローラークラス
 */
@RestController
@RequestMapping("/todos")
class TodoController(private val todoService: TodoService) {

    /**
     * 全てのTodoを取得する
     */
    @GetMapping
    fun getAllTodos(): List<TodoResponse> {
        return todoService.getAllTodos().map { TodoResponse.fromDomain(it) }
    }

    /**
     * 指定したIDのTodoを取得する
     */
    @GetMapping("/{id}")
    fun getTodoById(@PathVariable id: String): TodoResponse {
        val todo = todoService.getTodoById(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Todo not found with id: $id")
        return TodoResponse.fromDomain(todo)
    }

    /**
     * 新しいTodoを作成する
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createTodo(@Valid @RequestBody request: CreateTodoRequest): TodoResponse {
        val todo = todoService.createTodo(
            text = request.text,
            category = request.category
        )
        return TodoResponse.fromDomain(todo)
    }

    /**
     * Todoを更新する
     */
    @PutMapping("/{id}")
    fun updateTodo(
        @PathVariable id: String,
        @Valid @RequestBody request: UpdateTodoRequest
    ): TodoResponse {
        val todo = todoService.updateTodo(
            id = id,
            text = request.text,
            category = request.category
        ) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Todo not found with id: $id")
        return TodoResponse.fromDomain(todo)
    }

    /**
     * Todoを完了済みにする
     */
    @PatchMapping("/{id}/complete")
    fun markTodoAsCompleted(@PathVariable id: String): TodoResponse {
        val todo = todoService.markTodoAsCompleted(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Todo not found with id: $id")
        return TodoResponse.fromDomain(todo)
    }

    /**
     * Todoを未完了に戻す
     */
    @PatchMapping("/{id}/incomplete")
    fun markTodoAsIncomplete(@PathVariable id: String): TodoResponse {
        val todo = todoService.markTodoAsIncomplete(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Todo not found with id: $id")
        return TodoResponse.fromDomain(todo)
    }

    /**
     * Todoを削除する
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteTodo(@PathVariable id: String) {
        todoService.deleteTodo(id)
    }

    /**
     * すべてのTodoを削除する
     */
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteAllTodos() {
        todoService.deleteAllTodos()
    }

    /**
     * カテゴリー別にTodoを削除する
     */
    @DeleteMapping("/category/{category}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteTodosByCategory(@PathVariable category: String) {
        todoService.deleteTodosByCategory(category)
    }
} 