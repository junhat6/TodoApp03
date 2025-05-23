# Vue 3 + TypeScript + Vite

This template should help get you started developing with Vue 3 and TypeScript in Vite. The template uses Vue 3 `<script setup>` SFCs, check out the [script setup docs](https://v3.vuejs.org/api/sfc-script-setup.html#sfc-script-setup) to learn more.

Learn more about the recommended Project Setup and IDE Support in the [Vue Docs TypeScript Guide](https://vuejs.org/guide/typescript/overview.html#project-setup).

## 環境変数

`.env.local`ファイルで以下の環境変数を設定できます：

- `VITE_USE_MSW`: モックサービスワーカー（MSW）を有効にするかどうか
  - `true`: モックAPIを使用（デフォルト）
  - `false`: 実際のバックエンドAPIを使用
  
例：
```
VITE_USE_MSW=false
```

## 操作手順

### 開発環境の起動

1. 依存関係のインストール
```bash
npm install
```

2. 開発サーバーの起動
```bash
npm run dev
```

3. ブラウザでアクセス
```
http://localhost:5173
```

### ビルド

本番用ビルドを作成する場合：
```bash
npm run build
```

### テスト

ユニットテストの実行：
```bash
npm run test
```

### Todoの操作方法

1. 新規Todoの作成
   - 画面上部の入力フィールドにタスクを入力
   - Enterキーを押すか、追加ボタンをクリック

2. Todoの編集
   - タスクのテキストをクリックして編集モードに
   - 変更後、Enterキーを押すか、他の場所をクリック

3. Todoの完了/未完了
   - チェックボックスをクリックして状態を切り替え

4. Todoの削除
   - 削除ボタン（ゴミ箱アイコン）をクリック

5. フィルタリング
   - 画面上部のフィルターボタンで表示を切り替え
   - 全て/未完了/完了済み
