package com.example.todo.domain.model

import java.util.UUID

/**
 * TodoエンティティのID（値オブジェクト）
 * 
 * DDDにおける値オブジェクト（Value Object）の特徴：
 * - 同一性ではなく同値性で比較される
 * - 不変である（イミュータブル）
 * - 置換可能である
 * - 副作用のない振る舞いを持つ
 * 
 * IDを値オブジェクトにする利点：
 * - 型安全性の向上（文字列IDとの混同を防ぐ）
 * - ドメインの意図の明確化
 * - 不正な値の代入防止
 * - IDに関連する振る舞いの集約
 * 
 * Kotlinの data class の活用：
 * - equals(), hashCode() が値で比較される
 * - toString() のカスタマイズが可能
 * - copy() による安全な複製
 * 
 * UUID（Universally Unique Identifier）の採用：
 * - 分散システムでの一意性保証
 * - データベースの自動生成IDに依存しない
 * - 予測不可能性によるセキュリティ向上
 */
data class TodoId(val value: UUID) {
    /**
     * 文字列表現への変換
     * 
     * UUID.toString() の委譲：
     * - 標準的なUUID文字列形式（ハイフン区切り）
     * - ログ出力やデバッグ時の可読性
     * - JSON変換時の一貫性
     */
    override fun toString(): String = value.toString()
} 