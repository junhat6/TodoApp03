<script setup lang="ts">
// Vue 3のComposition APIから必要な関数をインポート
// ref: リアクティブなデータを作成
// computed: 算出プロパティを作成（依存関係に基づいて自動更新）
import { ref, computed } from 'vue';

// i18n（国際化）のコンポーザブル関数をインポート
// t関数で翻訳テキストにアクセス可能
import { useI18n } from 'vue-i18n';

// 子コンポーネントをインポート
import TodoInput from '../components/features/todo/TodoInput.vue';
import TodoList from '../components/features/todo/TodoList.vue';
import DeleteAllButton from '../components/features/todo/DeleteAllButton.vue';
import AppHeader from '../components/layout/AppHeader.vue';

// Piniaストアをインポート
// アプリケーション全体の状態管理を担当
import { useTodoStore } from '../store/todo';

// カテゴリー定数をインポート
import { CATEGORIES, ALL_CATEGORIES } from '../constants/categories';
import type { CategoryId } from '../constants/categories';

// i18nのt関数を取得（翻訳用）
const { t } = useI18n();

// Todoストアインスタンスを取得
// 状態管理の中央集権化
const store = useTodoStore();

// 現在選択されているカテゴリーを管理するリアクティブな変数
// デフォルトは「すべて」のカテゴリー
const selectedCategory = ref<CategoryId>(ALL_CATEGORIES);

// カテゴリ別の残りタスク数を計算する算出プロパティ
// selectedCategoryやstore.todosが変更されると自動的に再計算される
const categoryRemaining = computed(() => {
  return store.getRemainingByCategory(selectedCategory.value);
});

// すべてのタスクの残り数を取得する算出プロパティ
// ストアのgetterを使用してリアクティブに値を取得
const totalRemaining = computed(() => store.remaining);

// カテゴリタブの選択処理
// ユーザーがタブをクリックした時に呼び出される
function selectCategory(category: CategoryId) {
  selectedCategory.value = category;
}
</script>

<template>
  <div class="todo-view">
    <div class="todo-container">
      <div class="card">
        <AppHeader />
        
        <div class="app-actions">
          <TodoInput :selectedCategory="selectedCategory" />
        </div>
        
        <div class="category-tabs">
          <button 
            v-for="category in CATEGORIES" 
            :key="category"
            @click="selectCategory(category)"
            :class="['tab', { 'active': selectedCategory === category }]"
          >
            {{ t(`app.categories.${category}`) }}
          </button>
        </div>
        
        <TodoList :selectedCategory="selectedCategory" />
        
        <div class="card-footer">
          <div class="task-stats">
            <p class="remaining-count" v-if="selectedCategory !== ALL_CATEGORIES">
              <span class="category-name">{{ t(`app.categories.${selectedCategory}`) }}:</span>
              {{ t('app.task.remaining', { count: categoryRemaining }) }}
            </p>
            <p class="remaining-count">
              <span class="category-name">{{ t('app.categories.all') }}:</span>
              {{ t('app.task.remaining', { count: totalRemaining }) }}
            </p>
          </div>
          <DeleteAllButton :selectedCategory="selectedCategory" />
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.todo-view {
  flex: 1;
  padding: 2rem;
  background-color: var(--background);
  display: flex;
  justify-content: center;
  align-items: center;
  overflow-y: auto;
}

.todo-container {
  width: 100%;
  max-width: 600px;
}

.card {
  background-color: var(--card);
  border-radius: var(--radius);
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
  overflow: hidden;
  transition: background-color 0.3s, color 0.3s;
}

.app-actions {
  padding: 1.5rem;
  padding-bottom: 0;
}

.category-tabs {
  display: flex;
  padding: 0 1.5rem;
  margin-top: 1.5rem;
  border-bottom: 1px solid var(--border);
  overflow-x: auto;
}

.tab {
  background: transparent;
  border: none;
  border-bottom: 2px solid transparent;
  color: var(--muted-foreground);
  cursor: pointer;
  font-size: 0.875rem;
  padding: 0.75rem 1rem;
  transition: all var(--transition-fast);
  white-space: nowrap;
}

.tab:hover {
  color: var(--card-foreground);
}

.tab.active {
  border-bottom: 2px solid var(--primary);
  color: var(--primary);
}

.card-footer {
  padding: 1.5rem;
  border-top: 1px solid var(--border);
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.task-stats {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.remaining-count {
  color: var(--muted-foreground);
  font-size: 0.875rem;
}

.category-name {
  font-weight: 500;
  color: var(--card-foreground);
}

/* モバイル対応 */
@media (max-width: 768px) {
  .todo-view {
    padding: 1rem;
  }
  
  .app-actions {
    padding: 1rem;
    padding-bottom: 0;
  }
  
  .category-tabs {
    padding: 0 1rem;
  }
  
  .card-footer {
    padding: 1rem;
    flex-direction: column;
    align-items: flex-start;
    gap: 1rem;
  }
}
</style> 