package com.example.todo.infrastructure.repository

import com.example.todo.infrastructure.entity.TodoEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

/**
 * Spring Data JPA を使用したTodoエンティティのリポジトリ
 * 
 * Spring Data JPA の特徴：
 * - JpaRepository継承によるCRUD操作の自動提供
 * - メソッド名によるクエリ自動生成
 * - カスタムクエリの@Query サポート
 * - ページング・ソート機能の組み込み
 * 
 * JpaRepository<TodoEntity, UUID> の説明：
 * - TodoEntity: 操作対象のエンティティ型
 * - UUID: エンティティの主キー型
 * - 標準的なCRUD操作が自動的に提供される
 * 
 * @Repository アノテーションの効果：
 * - Spring のコンポーネントスキャン対象
 * - JPA例外のSpring例外への変換
 * - トランザクション対象として認識
 */
@Repository
interface SpringDataJpaTodoRepository : JpaRepository<TodoEntity, UUID> {
    
    /**
     * カテゴリー別削除のカスタムクエリ
     * 
     * @Modifying アノテーション：
     * - 更新系クエリ（INSERT、UPDATE、DELETE）に必須
     * - 読み取り専用でないことをSpringに通知
     * - clearAutomatically = true で1次キャッシュクリア可能
     * 
     * @Query アノテーション：
     * - カスタムJPQLクエリの定義
     * - ネイティブSQLも nativeQuery = true で可能
     * - パラメータバインディング（:category）
     * 
     * JPQL（Java Persistence Query Language）の特徴：
     * - エンティティベースのクエリ言語
     * - データベース非依存
     * - オブジェクト指向的な記述
     */
    @Modifying
    @Query("DELETE FROM TodoEntity t WHERE t.category = :category")
    fun deleteByCategory(category: String)
} 