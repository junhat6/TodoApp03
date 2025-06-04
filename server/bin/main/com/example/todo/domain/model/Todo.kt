package com.example.todo.domain.model

import java.time.LocalDateTime
import java.util.UUID

/**
 * Todoエンティティ（DDDにおける集約ルート）
 * 
 * Domain-Driven Design (DDD) における重要な概念：
 * - エンティティ: 一意のIDを持ち、ライフサイクルを通じて追跡される重要なオブジェクト
 * - 集約ルート: 関連するオブジェクト群の整合性を保つ責任を持つエンティティ
 * - 不変条件: ビジネスルールを満たす状態を常に維持する
 * 
 * Kotlinの設計パターン：
 * - private constructor: オブジェクトの生成を制御し、不正な状態での作成を防ぐ
 * - private var: 外部からの直接変更を防ぎ、メソッドを通じてのみ状態変更を許可
 * - companion object: Kotlinの静的メソッドに相当、ファクトリーメソッドを提供
 */
class Todo private constructor(
    val id: TodoId,                    // 一意識別子（不変）
    private var text: String,          // タスクの内容（可変だが制御された変更のみ）
    private var category: String,      // カテゴリー（可変だが制御された変更のみ）
    private var completed: Boolean,    // 完了状態（可変だが制御された変更のみ）
    private val createdAt: LocalDateTime,  // 作成日時（不変）
    private var updatedAt: LocalDateTime   // 更新日時（内部で管理）
) {
    /**
     * ファクトリーメソッドを提供するcompanion object
     * 
     * Kotlinのcompanion objectの特徴：
     * - クラスのインスタンスを作らずにアクセス可能
     * - Javaの static メソッドに相当
     * - オブジェクト生成の責任を集約し、不正な状態での作成を防ぐ
     */
    companion object {
        /**
         * 新しいTodoを作成するファクトリーメソッド
         * 
         * ファクトリーパターンの利点：
         * - オブジェクト生成のロジックを一箇所に集約
         * - 生成時のバリデーションや初期化処理を統一
         * - 将来の変更に対して柔軟性を保つ
         */
        fun create(text: String, category: String): Todo {
            val now = LocalDateTime.now()
            return Todo(
                id = TodoId(UUID.randomUUID()),  // UUIDで一意のIDを生成
                text = text,
                category = category,
                completed = false,               // 新規作成時は必ず未完了
                createdAt = now,
                updatedAt = now
            )
        }
    }

    // ゲッターメソッド群
    // Kotlinでは通常プロパティアクセスを使うが、private varの場合は
    // 明示的にゲッターメソッドを定義してカプセル化を保つ
    fun getText(): String = text
    fun getCategory(): String = category
    fun isCompleted(): Boolean = completed
    fun getCreatedAt(): LocalDateTime = createdAt
    fun getUpdatedAt(): LocalDateTime = updatedAt

    /**
     * テキストを更新する
     * 
     * ビジネスロジックの考慮点：
     * - 単純な setter ではなく、ビジネス的な意味のあるメソッド名
     * - 更新時に updatedAt を自動更新
     * - 将来的にバリデーションやイベント発行を追加可能
     */
    fun updateText(text: String) {
        this.text = text
        this.updatedAt = LocalDateTime.now()
    }

    /**
     * カテゴリーを更新する
     */
    fun updateCategory(category: String) {
        this.category = category
        this.updatedAt = LocalDateTime.now()
    }

    /**
     * Todoを完了状態にマークする
     * 
     * ビジネスロジックの実装：
     * - 既に完了している場合は何もしない（冪等性）
     * - 状態変更時に更新日時を自動更新
     * - メソッド名がビジネス的な意図を明確に表現
     */
    fun markAsCompleted() {
        if (!this.completed) {
            this.completed = true
            this.updatedAt = LocalDateTime.now()
        }
    }

    /**
     * Todoを未完了状態に戻す
     * 
     * 冪等性: 同じ操作を複数回実行しても結果が変わらない性質
     * これにより、ネットワークエラーなどでリトライが発生しても安全
     */
    fun markAsIncomplete() {
        if (this.completed) {
            this.completed = false
            this.updatedAt = LocalDateTime.now()
        }
    }
} 