<script setup lang="ts">
import { onMounted, computed, defineProps } from 'vue';
import { useI18n } from 'vue-i18n';
import { useTodoStore } from '../store/todo';
import TodoItem from './TodoItem.vue';
import { ALL_CATEGORIES } from '../constants/categories';
import type { CategoryId } from '../constants/categories';

const { t } = useI18n();

const props = defineProps({
  selectedCategory: {
    type: String as () => CategoryId,
    default: ALL_CATEGORIES
  }
});

const store = useTodoStore();

// 選択されたカテゴリーでフィルタリングされたTodo
const filteredTodos = computed(() => {
  return store.getTodosByCategory(props.selectedCategory);
});

// ローディング状態の取得
const isLoading = computed(() => store.isLoading);

// エラーメッセージの取得
const errorMessage = computed(() => store.errorMessage);

// イベントハンドラー
function handleToggle(id: number) {
  store.toggleTodo(id);
}

function handleRemove(id: number) {
  store.removeTodo(id);
}

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
