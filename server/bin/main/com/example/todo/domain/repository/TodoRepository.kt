package com.example.todo.domain.repository

import com.example.todo.domain.model.Todo
import com.example.todo.domain.model.TodoId

/**
 * Todoリポジトリのインターフェース
 * 
 * DDDにおけるリポジトリパターンの役割：
 * - ドメインオブジェクトの永続化の抽象化
 * - データアクセス技術からドメイン層を分離
 * - メモリ上のコレクションのように振る舞う
 * - テストにおけるモック化の容易性
 * 
 * インターフェース分離の原則（ISP）：
 * - ドメイン層は永続化の詳細を知らない
 * - データベース技術の変更がドメイン層に影響しない
 * - ビジネスロジックの純粋性を保つ
 * 
 * 命名規則のベストプラクティス：
 * - findXxx(): 検索系操作（0件〜複数件）
 * - save(): 保存操作（新規作成・更新の両方）
 * - delete(): 削除操作
 */
interface TodoRepository {
    /**
     * 全てのTodoを取得する
     * 
     * @return 全Todoのリスト（0件の場合は空リスト）
     */
    fun findAll(): List<Todo>

    /**
     * IDによってTodoを取得する
     * 
     * @param id 検索対象のTodoID
     * @return 見つかったTodo、存在しない場合はnull
     */
    fun findById(id: TodoId): Todo?

    /**
     * Todoを保存する（新規作成・更新の両方に対応）
     * 
     * Repository パターンの重要な特徴：
     * - 新規作成と更新を同一メソッドで処理
     * - 呼び出し側が意識する必要がない
     * - データベースの主キー存在確認は実装側の責任
     * 
     * @param todo 保存対象のTodo
     * @return 保存されたTodo（データベース生成値が反映される）
     */
    fun save(todo: Todo): Todo

    /**
     * IDによってTodoを削除する
     * 
     * 冪等性の確保：
     * - 存在しないIDでも例外を投げない
     * - 削除済みリソースの再削除も安全
     * 
     * @param id 削除対象のTodoID
     */
    fun delete(id: TodoId)

    /**
     * 全てのTodoを削除する
     * 
     * 一括操作の抽象化：
     * - 効率的な一括削除をデータベース層で実装
     * - ループによる個別削除よりもパフォーマンス向上
     */
    fun deleteAll()

    /**
     * カテゴリー別にTodoを削除する
     * 
     * 条件付き一括削除：
     * - ドメイン知識（カテゴリー）に基づく操作
     * - SQLのWHERE句による効率的な削除
     * 
     * @param category 削除対象のカテゴリー
     */
    fun deleteByCategory(category: String)
} 