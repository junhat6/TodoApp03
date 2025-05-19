package com.example.todo.infrastructure.repository

import com.example.todo.infrastructure.entity.TodoEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

/**
 * TodoEntityのJPAリポジトリ
 */
@Repository
interface TodoJpaRepository : JpaRepository<TodoEntity, UUID> 