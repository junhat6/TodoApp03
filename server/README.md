# Todo API サーバー

DDD（ドメイン駆動設計）の原則に基づいて作成されたToDoアプリケーションのバックエンドです。

## 技術スタック

- Spring Boot 3.2.4
- Kotlin 1.9.x
- Spring Data JPA
- PostgreSQL / H2 Database
- Flyway (DBマイグレーション)

## プロジェクト構成

DDDのレイヤード・アーキテクチャに沿った構成になっています：

- **プレゼンテーション層**: コントローラー、DTOクラス、例外処理
- **アプリケーション層**: アプリケーションサービス
- **ドメイン層**: ドメインモデル、値オブジェクト、リポジトリインターフェース
- **インフラストラクチャ層**: リポジトリ実装、DBエンティティ、設定クラス

## 開発環境のセットアップ

### 必須条件

- JDK 17以上
- PostgreSQL（本番環境用）
- Gradleビルドツール

### 開発環境の起動方法

1. リポジトリをクローン
   ```
   git clone <repository-url>
   cd server
   ```

2. PostgreSQLを起動（または適当なDocker環境）
   ```
   docker run --name postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=todo_app -p 5432:5432 -d postgres
   ```

3. アプリケーションをビルド＆実行
   ```
   ./gradlew bootRun
   ```

4. アプリケーションにアクセス
   ```
   http://localhost:8080/api/todos
   ```

## APIエンドポイント

### Todo API

| メソッド | エンドポイント | 説明 |
|--------|--------------|------|
| GET    | /api/todos      | 全てのTodoを取得 |
| GET    | /api/todos/{id} | 指定IDのTodoを取得 |
| POST   | /api/todos      | 新しいTodoを作成 |
| PUT    | /api/todos/{id} | 指定IDのTodoを更新 |
| PATCH  | /api/todos/{id}/complete | Todoを完了済みにする |
| PATCH  | /api/todos/{id}/incomplete | Todoを未完了に戻す |
| DELETE | /api/todos/{id} | 指定IDのTodoを削除 | 