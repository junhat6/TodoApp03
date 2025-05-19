package com.example.todo.infrastructure.repository

import com.example.todo.infrastructure.entity.TodoEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface SpringDataJpaTodoRepository : JpaRepository<TodoEntity, UUID> {
    @Modifying
    @Query("DELETE FROM TodoEntity t WHERE t.category = :category")
    fun deleteByCategory(category: String)
} 