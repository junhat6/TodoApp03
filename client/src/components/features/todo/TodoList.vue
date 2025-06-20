<script setup lang="ts">
// Vue 3のComposition APIから必要な関数をインポート
// onMounted: コンポーネントがマウントされた時に実行
// computed: 算出プロパティを作成
// defineProps: プロパティの型定義
import { onMounted, computed, defineProps } from 'vue';

// i18n（国際化）のコンポーザブル関数をインポート
import { useI18n } from 'vue-i18n';

// Piniaストアをインポート
import { useTodoStore } from '../../../store/todo';

// 子コンポーネントをインポート
// 個々のTodoアイテムを表示する責任を持つ
import TodoItem from './TodoItem.vue';

// カテゴリー関連の定数をインポート
import { ALL_CATEGORIES } from '../../../constants/categories';
import type { CategoryId } from '../../../constants/categories';

// i18nのt関数を取得（翻訳用）
const { t } = useI18n();

// プロパティの型定義
// 親コンポーネントから現在選択されているカテゴリーを受け取る
const props = defineProps({
  selectedCategory: {
    type: String as () => CategoryId,
    default: ALL_CATEGORIES
  }
});

// Todoストアインスタンスを取得
const store = useTodoStore();

// 選択されたカテゴリーでフィルタリングされたTodoを取得する算出プロパティ
// selectedCategoryが変更されると自動的に再計算される
const filteredTodos = computed(() => {
  return store.getTodosByCategory(props.selectedCategory);
});

// ローディング状態の取得
// API通信中などの状態を表示するために使用
const isLoading = computed(() => store.isLoading);

// エラーメッセージの取得
// API通信エラーなどを表示するために使用
const errorMessage = computed(() => store.errorMessage);

// Todoの完了状態切り替えイベントハンドラー
// 子コンポーネント（TodoItem）からのイベントを受け取る
function handleToggle(id: number) {
  store.toggleTodo(id);
}

// Todo削除イベントハンドラー
// 子コンポーネント（TodoItem）からのイベントを受け取る
function handleRemove(id: number) {
  store.removeTodo(id);
}

// コンポーネントがマウントされた時に実行
// 初期データの読み込みを行う
onMounted(async () => {
  await store.loadTodo();
});
</script>

<template>
  <div class="todo-list-container" role="region" aria-label="タスクリスト">
    <!-- ローディング表示 -->
    <div v-if="isLoading" class="loading-state">
      <div class="spinner"></div>
      <p>{{ t('app.task.loading') }}</p>
    </div>

    <!-- エラー表示 -->
    <div v-else-if="errorMessage" class="error-state">
      <p>{{ errorMessage }}</p>
    </div>

    <!-- タスクリスト表示 -->
    <ul v-else-if="filteredTodos.length > 0" class="todo-list" role="list">
      <TodoItem 
        v-for="todo in filteredTodos" 
        :key="todo.id" 
        :todo="todo"
        @toggle="handleToggle"
        @remove="handleRemove"
      />
    </ul>

    <!-- 空の状態表示 -->
    <div v-else class="empty-state">
      <p v-if="props.selectedCategory === ALL_CATEGORIES">
        {{ t('app.task.empty') }}
      </p>
      <p v-else>
        {{ t('app.task.emptyCategory', { category: t(`app.categories.${props.selectedCategory}`) }) }}
      </p>
    </div>
  </div>
</template>

<style scoped>
.todo-list-container {
  padding: 1.5rem;
  min-height: 200px;
  position: relative;
}

.todo-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.empty-state {
  text-align: center;
  padding: 2rem 0;
  color: var(--muted-foreground);
}

.loading-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 2rem 0;
  color: var(--muted-foreground);
}

.error-state {
  text-align: center;
  padding: 1rem;
  margin: 1rem 0;
  border-radius: var(--radius);
  background-color: rgba(239, 68, 68, 0.1);
  color: var(--destructive);
}

.spinner {
  width: 30px;
  height: 30px;
  border: 3px solid rgba(99, 102, 241, 0.3);
  border-radius: 50%;
  border-top-color: var(--primary);
  animation: spin 1s ease-in-out infinite;
  margin-bottom: 0.5rem;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}
</style>
