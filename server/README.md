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
   ```bash
   git clone <repository-url>
   cd TodoApp03
   ```

2. PostgreSQLをDocker Composeで起動
   ```bash
   docker-compose up -d
   ```

3. アプリケーションをビルド＆実行
   ```bash
   cd server
   ./gradlew bootRun
   ```

4. アプリケーションにアクセス
   ```
   http://localhost:8080/api/todos
   ```

5. 停止する場合
   ```bash
   # アプリケーションを停止（Ctrl+C）
   # PostgreSQLを停止
   docker-compose down
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

## 操作手順

### データベース操作

1. マイグレーションの実行
```bash
./gradlew flywayMigrate
```

2. マイグレーションのリセット（開発環境用）
```bash
./gradlew flywayClean flywayMigrate
```

### テスト

1. ユニットテストの実行
```bash
./gradlew test
```

2. 統合テストの実行
```bash
./gradlew integrationTest
```

### デバッグ

1. ログの確認
```bash
tail -f build/tmp/bootRun/output.txt
```

2. データベース接続の確認
```bash
psql -h localhost -U postgres -d todo_app
```

### トラブルシューティング

1. ポートが使用中の場合
```bash
lsof -i :8080
kill -9 <PID>
```

2. データベース接続エラーの場合
- PostgreSQLが起動していることを確認
- 接続情報（application.yml）を確認
- データベースが存在することを確認 