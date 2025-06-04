package com.example.todo.infrastructure.entity

import com.example.todo.domain.model.Todo
import com.example.todo.domain.model.TodoId
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

/**
 * TodoエンティティのJPAマッピングクラス
 * 
 * JPA（Java Persistence API）エンティティの役割：
 * - オブジェクト関係マッピング（ORM）
 * - データベーステーブルとKotlinクラスの対応
 * - 永続化ライフサイクルの管理
 * 
 * エンティティとドメインモデルの分離：
 * - JPAの制約（デフォルトコンストラクタ等）からドメインモデルを保護
 * - データベース中心設計とドメイン中心設計の橋渡し
 * - 技術的関心事とビジネス関心事の分離
 * 
 * Jakarta EE への移行：
 * - javax.persistence → jakarta.persistence
 * - Spring Boot 3.x での標準
 */
@Entity
@Table(name = "todos")
class TodoEntity(
    /**
     * 主キー設定
     * 
     * @Id: JPA主キーマーカー
     * @Column設定の詳細：
     * - nullable = false: NOT NULL制約
     * - updatable = false: 更新不可（主キーの不変性）
     * 
     * UUID vs 自動生成ID：
     * - UUID: 分散システムでの一意性保証
     * - 自然キーではなく代理キー（サロゲートキー）
     */
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    val id: UUID,

    /**
     * テキストフィールド
     * 
     * length = 100: VARCHAR(100) として定義
     * - データベース制約とアプリケーション制約の一致
     * - パフォーマンスとストレージ効率の考慮
     */
    @Column(name = "text", nullable = false, length = 100)
    var text: String,

    /**
     * カテゴリーフィールド
     * 
     * 正規化 vs 非正規化の設計判断：
     * - この実装では非正規化（文字列として保存）
     * - 正規化する場合は別テーブルでカテゴリー管理
     * - 要件に応じた適切な設計選択
     */
    @Column(name = "category", nullable = false, length = 50)
    var category: String,

    /**
     * 完了状態フィールド
     * 
     * Boolean → データベース型：
     * - PostgreSQL: BOOLEAN
     * - MySQL: TINYINT(1)
     * - H2: BOOLEAN
     * JPAが自動的にマッピング
     */
    @Column(name = "completed", nullable = false)
    var completed: Boolean,

    /**
     * 作成日時フィールド
     * 
     * updatable = false: 作成後の変更を禁止
     * - 監査要件の実現
     * - データの整合性保証
     * 
     * LocalDateTime の使用：
     * - タイムゾーンを含まない日時
     * - アプリケーションでタイムゾーン管理
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime,

    /**
     * 更新日時フィールド
     * 
     * 楽観的ロックとの関係：
     * - @Version と組み合わせて使用することも可能
     * - 同時更新の制御
     */
    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime
) {
    /**
     * ドメインモデルからエンティティへの変換ファクトリーメソッド
     * 
     * 静的ファクトリーメソッドの利点：
     * - 意図が明確な名前付け
     * - オブジェクト生成の制御
     * - 将来の拡張性
     */
    companion object {
        fun fromDomain(todo: Todo): TodoEntity {
            return TodoEntity(
                id = todo.id.value,                // 値オブジェクトから値を抽出
                text = todo.getText(),
                category = todo.getCategory(),
                completed = todo.isCompleted(),
                createdAt = todo.getCreatedAt(),
                updatedAt = todo.getUpdatedAt()
            )
        }
    }

    /**
     * エンティティからドメインモデルへの変換
     * 
     * 責任の分離：
     * - エンティティ自身が変換処理を知っている
     * - 外部クラスでの変換処理より凝集度が高い
     */
    fun toDomain(): Todo {
        return TodoEntityMapper.toDomain(this)
    }
}

/**
 * TodoEntityからTodoドメインオブジェクトへの変換を行うマッパー
 * 
 * リフレクションを使用する理由：
 * - Todoクラスのprivate constructorにアクセス
 * - ドメインモデルの不変性を保ちながら復元
 * - ファクトリーメソッドでは表現できない既存オブジェクトの復元
 * 
 * セキュリティとパフォーマンスの考慮：
 * - リフレクションのアクセス制御変更
 * - 使用後のアクセス制御復元
 * - パフォーマンスが重要な場合は他の手法も検討
 * 
 * object 宣言（Kotlinのシングルトン）：
 * - インスタンス化が不要
 * - 状態を持たないユーティリティクラス
 */
object TodoEntityMapper {
    /**
     * エンティティからドメインオブジェクトへの変換
     * 
     * リフレクション使用の詳細：
     * 1. private constructor の取得
     * 2. アクセス制御の一時的解除
     * 3. newInstance でオブジェクト生成
     * 4. アクセス制御の復元
     * 
     * エラーハンドリング：
     * - リフレクション例外は実行時例外として伝播
     * - 開発時に問題を早期発見
     */
    fun toDomain(entity: TodoEntity): Todo {
        val todoClass = Todo::class.java
        val constructor = todoClass.getDeclaredConstructor(
            TodoId::class.java,
            String::class.java,
            String::class.java,
            Boolean::class.java,
            LocalDateTime::class.java,
            LocalDateTime::class.java
        )
        // private constructor への一時的アクセス許可
        constructor.isAccessible = true
        
        val todo = constructor.newInstance(
            TodoId(entity.id),           // UUID → TodoId 値オブジェクト
            entity.text,
            entity.category,
            entity.completed,
            entity.createdAt,
            entity.updatedAt
        )
        
        // セキュリティ：アクセス制御を元に戻す
        constructor.isAccessible = false
        return todo
    }
} 