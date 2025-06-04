package com.example.todo.presentation.dto

import com.example.todo.domain.model.Todo
import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

/**
 * TodoのレスポンスDTO（Data Transfer Object）
 * 
 * DTOパターンの目的：
 * - プレゼンテーション層とドメイン層の分離
 * - API仕様の安定性確保
 * - JSON形式での効率的なデータ転送
 * - 不要な内部情報の隠蔽
 * 
 * Kotlinの data class の特徴：
 * - equals(), hashCode(), toString() が自動生成
 * - copy() メソッドが自動生成（イミュータブルな更新）
 * - プロパティの分解宣言（Destructuring）に対応
 * 
 * Jackson アノテーションの活用：
 * @JsonFormat: 日時フィールドのJSON形式を制御
 * - ISO 8601 形式での統一的な日時表現
 * - フロントエンドでのパース処理の簡素化
 */
data class TodoResponse(
    val id: String,                    // UUID文字列として公開
    val text: String,                  // タスクの内容
    val category: String,              // カテゴリー名
    val completed: Boolean,            // 完了状態
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val createdAt: LocalDateTime,      // 作成日時（ISO 8601形式）
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val updatedAt: LocalDateTime       // 更新日時（ISO 8601形式）
) {
    /**
     * ドメインオブジェクトからDTOへの変換を行うファクトリーメソッド
     * 
     * companion object の活用：
     * - 静的メソッドの代替
     * - 型安全な変換処理の提供
     * - ドメインモデルの知識をDTOに局所化
     * 
     * 変換パターンのベストプラクティス：
     * - 一方向の変換（ドメイン → DTO）
     * - ドメインモデルの内部構造への依存を最小化
     * - 将来の変更に対する柔軟性の確保
     */
    companion object {
        fun fromDomain(todo: Todo): TodoResponse {
            return TodoResponse(
                id = todo.id.toString(),          // TodoId を String に変換
                text = todo.getText(),             // ゲッターメソッドを使用
                category = todo.getCategory(),
                completed = todo.isCompleted(),
                createdAt = todo.getCreatedAt(),
                updatedAt = todo.getUpdatedAt()
            )
        }
    }
}

/**
 * Todoの作成リクエストDTO
 * 
 * Bean Validation の活用：
 * - @NotBlank: null、空文字、空白文字のみを拒否
 * - @Size: 文字列長の制限
 * - Spring Boot が自動的にバリデーションを実行
 * 
 * バリデーションアノテーションの配置：
 * @field: Kotlinプロパティのバッキングフィールドに適用
 * - Java互換性の確保
 * - リフレクション経由でのアクセスに対応
 * 
 * デフォルト値の活用：
 * - クライアントが省略可能なフィールド
 * - ビジネス要件に応じた適切なデフォルト設定
 */
data class CreateTodoRequest(
    @field:NotBlank(message = "テキストは必須です")
    @field:Size(max = 100, message = "テキストは100文字以内で入力してください")
    val text: String,

    val category: String = "個人"      // デフォルトカテゴリー
)

/**
 * Todoの更新リクエストDTO
 * 
 * 作成と更新の分離：
 * - 異なるバリデーションルールに対応
 * - 将来的な仕様変更への柔軟性
 * - API仕様の明確化
 * 
 * 一貫性のあるバリデーション：
 * - CreateTodoRequest と同じ制約
 * - 同一のビジネスルールの適用
 * - メンテナンス性の向上
 */
data class UpdateTodoRequest(
    @field:NotBlank(message = "テキストは必須です")
    @field:Size(max = 100, message = "テキストは100文字以内で入力してください")
    val text: String,

    val category: String = "個人"      // デフォルトカテゴリー
) 