# TodoApp Frontend

Vue 3 + TypeScript + Vite で構築された現代的なTODOアプリケーションのフロントエンドです。

## 📁 ディレクトリ構造

本プロジェクトは保守性、拡張性、開発効率を重視した階層的なディレクトリ構造を採用しています。

```
src/
├── views/                    # 📄 ページレベルのコンポーネント
│   ├── index.ts             # Views エクスポート
│   ├── TodoView.vue         # TODOページ
│   └── HabitsView.vue       # 習慣ページ
├── components/              # 🧩 再利用可能なコンポーネント
│   ├── index.ts             # Components エクスポート
│   ├── ui/                  # 基本的なUIコンポーネント
│   ├── layout/              # レイアウトコンポーネント
│   │   ├── AppHeader.vue    # アプリケーションヘッダー
│   │   └── Sidebar.vue      # サイドバーナビゲーション
│   └── features/            # 機能特化コンポーネント
│       ├── todo/            # TODO機能
│       │   ├── TodoInput.vue
│       │   ├── TodoItem.vue
│       │   ├── TodoList.vue
│       │   └── DeleteAllButton.vue
│       └── habits/          # 習慣機能（将来拡張予定）
├── composables/             # 🔄 Vue Composition API関数
│   ├── index.ts             # Composables エクスポート
│   └── useLocalStorage.ts   # LocalStorage管理用
├── utils/                   # 🛠️ ユーティリティ関数
│   ├── index.ts             # Utils エクスポート
│   └── dateUtils.ts         # 日付操作関数
├── assets/                  # 🎨 静的リソース
│   ├── images/              # 画像ファイル
│   ├── icons/               # アイコン
│   └── styles/              # スタイルファイル
├── api/                     # 🌐 API関連
├── store/                   # 🗄️ 状態管理（Pinia）
├── types/                   # 📝 型定義
├── constants/               # 📋 定数
├── config/                  # ⚙️ 設定
├── i18n/                    # 🌍 国際化
└── mocks/                   # 🎭 モック
```

## 🎯 構造設計の利点

### 1. **明確な責任分離**
- **Views**: ページレベルのコンポーネント（ルーティング対象）
- **Components**: 再利用可能なUI部品（ビジネスロジックを含まない）
- **Composables**: ビジネスロジックとリアクティブ状態管理
- **Utils**: 純粋関数・ヘルパー関数

### 2. **機能ベースの組織化**
```
features/
├── todo/           # TODO関連のすべてのコンポーネント
├── habits/         # 習慣機能（将来拡張）
└── auth/           # 認証機能（将来拡張予定）
```

### 3. **拡張性とメンテナンス性**
- 新機能追加時も既存構造を維持
- 関連ファイルの論理的グループ化
- インポート/エクスポートの最適化

### 4. **開発効率の向上**
- `index.ts`ファイルによるクリーンなインポート
- TypeScriptの完全サポート
- ホットリロード対応

## 📖 開発ガイドライン

### コンポーネント作成時のルール

#### 1. Viewコンポーネント（ページレベル）
```vue
<!-- views/ExampleView.vue -->
<script setup lang="ts">
// ページレベルのロジック
// 複数のfeatureコンポーネントを組み合わせ
</script>
```

#### 2. Featureコンポーネント（機能特化）
```vue
<!-- components/features/example/ExampleCard.vue -->
<script setup lang="ts">
// 特定機能に特化したコンポーネント
// composablesを活用してロジック分離
</script>
```

#### 3. Layoutコンポーネント（レイアウト）
```vue
<!-- components/layout/Navigation.vue -->
<script setup lang="ts">
// アプリケーション全体で共通のレイアウト
</script>
```

#### 4. UIコンポーネント（基本部品）
```vue
<!-- components/ui/Button.vue -->
<script setup lang="ts">
// 再利用可能な基本UIコンポーネント
// プロパティベースの制御
</script>
```

### Composable作成のベストプラクティス

```typescript
// composables/useExample.ts
export function useExample() {
  // ロジックの集約
  // リアクティブ状態の管理
  // 副作用の処理
  
  return {
    // 公開するAPIのみエクスポート
  };
}
```

### インポートパターン

```typescript
// クリーンなインポート（推奨）
import { TodoView, HabitsView } from '@/views';
import { Sidebar, AppHeader } from '@/components';
import { useLocalStorage } from '@/composables';
import { formatDate } from '@/utils';

// 個別インポート（詳細制御が必要な場合）
import TodoInput from '@/components/features/todo/TodoInput.vue';
```

## 🚀 今後の拡張予定

### 1. UIコンポーネントライブラリ
```
components/ui/
├── Button.vue
├── Input.vue
├── Modal.vue
├── Card.vue
└── ...
```

### 2. 習慣トラッキング機能
```
features/habits/
├── HabitCard.vue
├── HabitForm.vue
├── HabitCalendar.vue
└── ...
```

### 3. 認証システム
```
features/auth/
├── LoginForm.vue
├── SignupForm.vue
└── UserProfile.vue
```

## 💡 技術的な特徴

### アーキテクチャパターン

#### 1. **MVVM + Composition API**
- **Model**: Pinia Store + API Layer
- **View**: Vue SFCs (Single File Components)
- **ViewModel**: Composables（ビジネスロジック分離）

#### 2. **レイヤードアーキテクチャ**
```
Presentation Layer  → Views + Components
Business Layer     → Composables + Store
Data Layer         → API + Utils
```

#### 3. **依存性の方向**
```
Views → Components → Composables → Utils
  ↓         ↓           ↓          ↓
 Store ←→ API    ←→ Config  ←→ Constants
```

### コーディング規約

#### ファイル命名規則
```
PascalCase:   コンポーネント（TodoView.vue）
camelCase:    関数・変数（useLocalStorage.ts）
kebab-case:   ディレクトリ（todo-list/）
UPPER_CASE:   定数（API_ENDPOINTS）
```

#### インポート順序
```typescript
// 1. Vue関連
import { ref, computed } from 'vue';
import { useI18n } from 'vue-i18n';

// 2. 外部ライブラリ
import axios from 'axios';

// 3. 内部モジュール（絶対パス）
import { useTodoStore } from '@/store';
import { formatDate } from '@/utils';

// 4. 相対パス
import TodoItem from './TodoItem.vue';
```

#### TypeScript活用
```typescript
// 型定義の分離
interface Props {
  title: string;
  completed?: boolean;
}

// Generic Composables
function useAPI<T>(endpoint: string) {
  // 型安全なAPI呼び出し
}

// Emit Events
interface Emits {
  (e: 'update:value', value: string): void;
  (e: 'delete', id: number): void;
}
```

## 🧪 テスト戦略

### テストファイル配置
```
src/
├── components/
│   └── __tests__/           # コンポーネントテスト
├── composables/
│   └── __tests__/           # Composableテスト
├── utils/
│   └── __tests__/           # ユーティリティテスト
└── views/
    └── __tests__/           # 統合テスト
```

### テストの種類
- **Unit Tests**: Utils + Composables
- **Component Tests**: 個別コンポーネント
- **Integration Tests**: Views + Feature combinations
- **E2E Tests**: ユーザーフロー全体

## 📊 パフォーマンス最適化

### 1. **レイジーローディング**
```typescript
// ルートレベルでの分割
const TodoView = () => import('@/views/TodoView.vue');
const HabitsView = () => import('@/views/HabitsView.vue');
```

### 2. **コンポーネント分割**
```typescript
// 大きなコンポーネントの分割
const HeavyComponent = defineAsyncComponent(
  () => import('./HeavyComponent.vue')
);
```

### 3. **メモ化戦略**
```typescript
// 算出プロパティの活用
const expensiveValue = computed(() => {
  return heavyCalculation(props.data);
});
```

## 🔧 開発ツール設定

### VS Code 推奨拡張機能
```json
{
  "recommendations": [
    "Vue.volar",
    "bradlc.vscode-tailwindcss",
    "esbenp.prettier-vscode",
    "dbaeumer.vscode-eslint"
  ]
}
```

### パスエイリアス設定
```typescript
// vite.config.ts
export default defineConfig({
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  }
});
```

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

## 🌟 このプロジェクトの特徴

### ✅ 採用している現代的技術
- **Vue 3 Composition API**: リアクティブプログラミング
- **TypeScript**: 型安全性とIntelliSense
- **Vite**: 高速開発サーバー
- **Pinia**: 軽量状態管理
- **Vue I18n**: 国際化対応
- **MSW**: モックAPI開発

### ✅ 実装されている機能
- サイドバーナビゲーション
- TODO CRUD操作
- カテゴリー別フィルタリング
- ダークモード対応
- レスポンシブデザイン
- 多言語対応（日本語・英語）

### ✅ 開発体験の向上
- ホットリロード
- TypeScript自動補完
- ESLintコード品質チェック
- Prettierフォーマット
- パスエイリアス（@/）

## 📚 学習・参考リソース

### 公式ドキュメント
- [Vue 3 Guide](https://vuejs.org/guide/)
- [TypeScript Handbook](https://www.typescriptlang.org/docs/)
- [Vite Guide](https://vitejs.dev/guide/)
- [Pinia Documentation](https://pinia.vuejs.org/)

### アーキテクチャ参考
- [Vue 3 Best Practices](https://vuejs.org/style-guide/)
- [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Component Design Patterns](https://www.patterns.dev/)

## 🤝 コントリビューション

プロジェクトへの貢献を歓迎します！

### 開発環境セットアップ
1. リポジトリをクローン
2. `npm install` で依存関係をインストール
3. `npm run dev` で開発サーバー起動
4. 新機能は `feature/` ブランチで開発

### プルリクエストガイドライン
- このディレクトリ構造に従う
- TypeScriptの型定義を適切に行う
- コンポーネントは単一責任の原則を守る
- テストコードを含める（推奨）

---

**このディレクトリ構造は、チーム開発での保守性と拡張性を重視して設計されました。新しい機能を追加する際は、この構造に従って開発することで、プロジェクト全体の品質を維持できます。**
