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
 * 
 * DDDにおけるアプリケーションサービスの役割：
 * - ユースケースの実装とオーケストレーション
 * - ドメインオブジェクト間の協調処理
 * - トランザクション境界の管理
 * - ドメインロジックは含まない（ドメインモデルに委譲）
 * 
 * Spring アノテーションの説明：
 * @Service: ビジネスロジック層のコンポーネントであることを示す
 * @Transactional: クラス全体でトランザクション管理を有効にする
 * 
 * トランザクション管理のベストプラクティス：
 * - 読み取り専用操作には readOnly = true を指定
 * - データベースパフォーマンスの向上とロックの軽減
 */
@Service
@Transactional
class TodoService(private val todoRepository: TodoRepository) {

    /**
     * 全てのTodoを取得する（読み取り専用操作）
     * 
     * @Transactional(readOnly = true) の効果：
     * - データベースのパフォーマンス最適化
     * - 不要な書き込みロックを避ける
     * - Hibernateでは1次キャッシュをクリアしないなどの最適化
     */
    @Transactional(readOnly = true)
    fun getAllTodos(): List<Todo> {
        return todoRepository.findAll()
    }

    /**
     * IDによってTodoを取得する（読み取り専用操作）
     * 
     * エラーハンドリングの考慮：
     * - UUID.fromString() は不正な文字列で例外を投げる
     * - 呼び出し側で適切にハンドリングされることを期待
     * - null 返却で「見つからない」ケースを表現
     */
    @Transactional(readOnly = true)
    fun getTodoById(id: String): Todo? {
        val todoId = TodoId(UUID.fromString(id))
        return todoRepository.findById(todoId)
    }

    /**
     * 新しいTodoを作成する
     * 
     * ドメイン駆動設計の実践：
     * - Todo.create() でドメインオブジェクトを生成
     * - ビジネスルールはドメインモデル側で実装
     * - アプリケーションサービスは協調処理のみ担当
     * 
     * トランザクション境界：
     * - メソッド開始時にトランザクション開始
     * - 正常終了時にコミット、例外時にロールバック
     */
    fun createTodo(text: String, category: String): Todo {
        val todo = Todo.create(text, category)
        return todoRepository.save(todo)
    }

    /**
     * Todoを更新する
     * 
     * 更新処理のパターン：
     * 1. 既存エンティティを取得
     * 2. ドメインメソッドで状態変更
     * 3. リポジトリで永続化
     * 
     * Kotlin の ?. オペレーター：
     * - null安全な呼び出し
     * - findById が null を返した場合、早期リターン
     */
    fun updateTodo(id: String, text: String, category: String): Todo? {
        val todoId = TodoId(UUID.fromString(id))
        val todo = todoRepository.findById(todoId) ?: return null

        // ドメインメソッドを使用して状態変更
        // ビジネスルール（更新日時の管理など）はドメインモデル側で実装
        todo.updateText(text)
        todo.updateCategory(category)

        return todoRepository.save(todo)
    }

    /**
     * Todoを完了済みにする
     * 
     * 冪等性の確保：
     * - markAsCompleted() は既に完了済みの場合何もしない
     * - 同じリクエストが複数回来ても安全
     * - RESTfulなAPIの原則に従う
     */
    fun markTodoAsCompleted(id: String): Todo? {
        val todoId = TodoId(UUID.fromString(id))
        val todo = todoRepository.findById(todoId) ?: return null

        todo.markAsCompleted()
        return todoRepository.save(todo)
    }

    /**
     * Todoを未完了に戻す
     * 
     * 対称的な操作の提供：
     * - markAsCompleted と対になる操作
     * - 同様に冪等性を確保
     */
    fun markTodoAsIncomplete(id: String): Todo? {
        val todoId = TodoId(UUID.fromString(id))
        val todo = todoRepository.findById(todoId) ?: return null

        todo.markAsIncomplete()
        return todoRepository.save(todo)
    }

    /**
     * Todoを削除する
     * 
     * 削除操作の特徴：
     * - 存在しないIDでも例外を投げない
     * - 冪等性を保つ（既に削除済みでも問題なし）
     * - 戻り値なし（Unit型）
     */
    fun deleteTodo(id: String) {
        val todoId = TodoId(UUID.fromString(id))
        todoRepository.delete(todoId)
    }

    /**
     * すべてのTodoを削除する
     * 
     * 一括操作の実装：
     * - データ量が多い場合のパフォーマンス考慮が必要
     * - 実際のプロダクションでは論理削除も検討
     */
    fun deleteAllTodos() {
        todoRepository.deleteAll()
    }

    /**
     * カテゴリー別にTodoを削除する
     * 
     * 条件付き一括削除：
     * - SQLのWHERE句による効率的な削除
     * - N+1問題を避ける実装
     */
    fun deleteTodosByCategory(category: String) {
        todoRepository.deleteByCategory(category)
    }
} 