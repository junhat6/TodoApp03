package com.example.todo.presentation.controller

import com.example.todo.application.service.TodoService
import com.example.todo.presentation.dto.CreateTodoRequest
import com.example.todo.presentation.dto.TodoResponse
import com.example.todo.presentation.dto.UpdateTodoRequest
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

/**
 * Todoのコントローラークラス
 * 
 * Spring MVCにおけるコントローラーの役割：
 * - HTTPリクエストの受信とレスポンスの返却
 * - リクエストデータの検証とDTO変換
 * - アプリケーションサービスへの処理委譲
 * - 適切なHTTPステータスコードの設定
 * 
 * Spring Web アノテーションの説明：
 * @RestController: @Controller + @ResponseBody の組み合わせ
 *                  戻り値が自動的にJSONに変換される
 * @RequestMapping: クラス全体に適用されるベースURL
 * 
 * RESTful APIの設計原則：
 * - リソース指向（/todos がリソース）
 * - HTTPメソッドで操作を表現（GET=取得、POST=作成、PUT=更新、DELETE=削除）
 * - 適切なステータスコードの使用
 */
@RestController
@RequestMapping("/todos")
class TodoController(private val todoService: TodoService) {

    /**
     * 全てのTodoを取得する
     * 
     * HTTPメソッド: GET /todos
     * 
     * RESTfulな設計の実践：
     * - 複数リソースの取得は GET + 複数形URL
     * - 200 OK がデフォルトステータス
     * - List<TodoResponse> が自動的にJSON配列に変換
     * 
     * DTOパターンの適用：
     * - ドメインオブジェクトを直接公開しない
     * - API仕様の安定性を保つ
     * - 内部構造の変更から外部インターフェースを保護
     */
    @GetMapping
    fun getAllTodos(): List<TodoResponse> {
        return todoService.getAllTodos().map { TodoResponse.fromDomain(it) }
    }

    /**
     * 指定したIDのTodoを取得する
     * 
     * HTTPメソッド: GET /todos/{id}
     * 
     * パスパラメータの活用：
     * @PathVariable: URLの{id}部分を引数にバインド
     * 
     * エラーハンドリング：
     * ResponseStatusException: Springのエラーハンドリング機能
     * - 適切なHTTPステータスコード（404 Not Found）
     * - エラーメッセージの提供
     * - Spring Boot が自動的にJSON エラーレスポンスを生成
     */
    @GetMapping("/{id}")
    fun getTodoById(@PathVariable id: String): TodoResponse {
        val todo = todoService.getTodoById(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Todo not found with id: $id")
        return TodoResponse.fromDomain(todo)
    }

    /**
     * 新しいTodoを作成する
     * 
     * HTTPメソッド: POST /todos
     * ステータスコード: 201 Created
     * 
     * リクエストボディの処理：
     * @RequestBody: JSONリクエストボディをDTOにデシリアライズ
     * @Valid: Bean Validationによる入力検証を実行
     * 
     * HTTPステータスコードの使い分け：
     * @ResponseStatus(HttpStatus.CREATED): 201 Created
     * - リソースの新規作成時に使用
     * - RESTful APIの標準的な実践
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createTodo(@Valid @RequestBody request: CreateTodoRequest): TodoResponse {
        val todo = todoService.createTodo(
            text = request.text,
            category = request.category
        )
        return TodoResponse.fromDomain(todo)
    }

    /**
     * Todoを更新する
     * 
     * HTTPメソッド: PUT /todos/{id}
     * 
     * PUTとPATCHの使い分け：
     * - PUT: リソース全体の置換
     * - PATCH: リソースの部分更新
     * この実装では全フィールドを更新するためPUTを使用
     * 
     * 名前付き引数（Kotlin）：
     * - 可読性の向上
     * - 引数の順序に依存しない
     */
    @PutMapping("/{id}")
    fun updateTodo(
        @PathVariable id: String,
        @Valid @RequestBody request: UpdateTodoRequest
    ): TodoResponse {
        val todo = todoService.updateTodo(
            id = id,
            text = request.text,
            category = request.category
        ) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Todo not found with id: $id")
        return TodoResponse.fromDomain(todo)
    }

    /**
     * Todoを完了済みにする
     * 
     * HTTPメソッド: PATCH /todos/{id}/complete
     * 
     * PATCHメソッドの活用：
     * - 部分的な状態変更に最適
     * - ビジネス的な意味を持つURL設計
     * - /complete というサブリソースで操作を表現
     * 
     * 冪等性の確保：
     * - 同じリクエストを複数回送信しても結果が変わらない
     * - ネットワークエラー時のリトライ安全性
     */
    @PatchMapping("/{id}/complete")
    fun markTodoAsCompleted(@PathVariable id: String): TodoResponse {
        val todo = todoService.markTodoAsCompleted(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Todo not found with id: $id")
        return TodoResponse.fromDomain(todo)
    }

    /**
     * Todoを未完了に戻す
     * 
     * HTTPメソッド: PATCH /todos/{id}/incomplete
     * 
     * 対称的なAPI設計：
     * - /complete と /incomplete で状態変更を表現
     * - 直感的で理解しやすいURL構造
     */
    @PatchMapping("/{id}/incomplete")
    fun markTodoAsIncomplete(@PathVariable id: String): TodoResponse {
        val todo = todoService.markTodoAsIncomplete(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Todo not found with id: $id")
        return TodoResponse.fromDomain(todo)
    }

    /**
     * Todoを削除する
     * 
     * HTTPメソッド: DELETE /todos/{id}
     * ステータスコード: 204 No Content
     * 
     * 削除操作のベストプラクティス：
     * @ResponseStatus(HttpStatus.NO_CONTENT): 204 No Content
     * - 削除成功時はレスポンスボディなし
     * - 存在しないリソースの削除も成功扱い（冪等性）
     * 
     * Unit型（Kotlin）：
     * - Java の void に相当
     * - 戻り値がないことを明示
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteTodo(@PathVariable id: String) {
        todoService.deleteTodo(id)
    }

    /**
     * すべてのTodoを削除する
     * 
     * HTTPメソッド: DELETE /todos
     * 
     * 一括削除API：
     * - コレクション全体への DELETE
     * - 管理機能として提供
     * - 本番環境では慎重な権限管理が必要
     */
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteAllTodos() {
        todoService.deleteAllTodos()
    }

    /**
     * カテゴリー別にTodoを削除する
     * 
     * HTTPメソッド: DELETE /todos/category/{category}
     * 
     * クエリパラメータ vs パスパラメータ：
     * - この実装ではパスパラメータを使用
     * - /todos?category={category} というクエリパラメータ方式も可能
     * - RESTful設計では、フィルター条件はクエリパラメータが一般的
     */
    @DeleteMapping("/category/{category}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteTodosByCategory(@PathVariable category: String) {
        todoService.deleteTodosByCategory(category)
    }
} 