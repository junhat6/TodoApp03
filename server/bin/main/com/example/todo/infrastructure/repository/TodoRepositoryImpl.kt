package com.example.todo.infrastructure.repository

import com.example.todo.domain.model.Todo
import com.example.todo.domain.model.TodoId
import com.example.todo.domain.repository.TodoRepository
import com.example.todo.infrastructure.entity.TodoEntity
import org.springframework.stereotype.Repository
import java.util.UUID

/**
 * TodoRepositoryの実装クラス（インフラストラクチャ層）
 * 
 * アダプターパターンの実装：
 * - ドメイン層のインターフェース（TodoRepository）を実装
 * - Spring Data JPAの機能をドメイン層に適応
 * - 外部技術（JPA）からドメイン層を保護
 * 
 * 依存性逆転の原則（DIP）の実践：
 * - ドメイン層が定義したインターフェースを実装
 * - インフラ層がドメイン層に依存（逆転）
 * - 技術的詳細がビジネスロジックに影響しない
 * 
 * Spring アノテーションの説明：
 * @Repository: データアクセス層のコンポーネント
 * - 例外変換機能（JPA例外 → Spring例外）
 * - コンポーネントスキャンの対象
 * 
 * コンストラクタインジェクション：
 * - Kotlinの主コンストラクタでDIを実現
 * - val による不変性の確保
 * - テストでのモック化が容易
 */
@Repository
class TodoRepositoryImpl(
    private val jpaRepository: SpringDataJpaTodoRepository
) : TodoRepository {

    /**
     * 全てのTodoを取得する
     * 
     * エンティティ → ドメインモデル変換：
     * - map関数による関数型プログラミングスタイル
     * - toDomain()でカプセル化された変換処理
     * - リスト全体の一括変換
     * 
     * パフォーマンス考慮：
     * - 件数が多い場合はページング検討
     * - 実際のプロダクションでは制限が必要
     */
    override fun findAll(): List<Todo> {
        return jpaRepository.findAll().map { it.toDomain() }
    }

    /**
     * IDによってTodoを取得する
     * 
     * Optional の Kotlin での扱い：
     * - map(): Optional内の値を変換
     * - orElse(null): 空の場合はnullを返す
     * - Java互換性を保ちながらKotlinらしく記述
     * 
     * 値オブジェクトの活用：
     * - TodoId.value でUUIDを取得
     * - 型安全性を保ちながらJPAと連携
     */
    override fun findById(id: TodoId): Todo? {
        return jpaRepository.findById(id.value).map { it.toDomain() }.orElse(null)
    }

    /**
     * Todoを保存する
     * 
     * ドメインモデル ↔ エンティティ変換パターン：
     * 1. fromDomain(): ドメインモデル → JPA エンティティ
     * 2. JPA による永続化処理
     * 3. toDomain(): JPA エンティティ → ドメインモデル
     * 
     * JPA の save() の特徴：
     * - 主キー存在確認による新規作成/更新の自動判定
     * - トランザクション境界での実際の永続化
     * - 楽観的ロック等の機能を透過的に提供
     */
    override fun save(todo: Todo): Todo {
        val entity = TodoEntity.fromDomain(todo)
        return jpaRepository.save(entity).toDomain()
    }

    /**
     * IDによってTodoを削除する
     * 
     * JPA削除操作の特徴：
     * - deleteById() は存在しないIDでも例外を投げない
     * - 冪等性が自然に確保される
     * - 物理削除（実際にレコードを削除）
     */
    override fun delete(id: TodoId) {
        jpaRepository.deleteById(id.value)
    }

    /**
     * 全てのTodoを削除する
     * 
     * 一括削除の実装：
     * - deleteAll() による効率的な削除
     * - 個別削除の繰り返しよりもパフォーマンス向上
     * - トランザクション内での一括処理
     */
    override fun deleteAll() {
        jpaRepository.deleteAll()
    }

    /**
     * カテゴリー別にTodoを削除する
     * 
     * Spring Data JPA のクエリメソッド活用：
     * - deleteByCategory(): メソッド名から自動的にクエリ生成
     * - WHERE category = ? の条件で削除
     * - カスタムクエリより簡潔で保守性が高い
     */
    override fun deleteByCategory(category: String) {
        jpaRepository.deleteByCategory(category)
    }
} 