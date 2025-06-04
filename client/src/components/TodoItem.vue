<script setup lang="ts">
// Vue 3のComposition APIから必要な関数をインポート
// defineProps: 親からのプロパティを定義
// defineEmits: 親へのイベントを定義
import { defineProps, defineEmits } from 'vue';

// Todoアイテムの型定義をインポート
import type { Todo } from '../types/todo';

// i18n（国際化）のコンポーザブル関数をインポート
import { useI18n } from 'vue-i18n';

// プロパティの型定義
// 親コンポーネントから個々のTodoオブジェクトを受け取る
const props = defineProps<{
  todo: Todo
}>();

// イベントの型定義
// 親コンポーネントに対して発火するイベントを定義
const emit = defineEmits<{
  (e: 'toggle', id: number): void    // 完了状態切り替えイベント
  (e: 'remove', id: number): void    // 削除イベント
}>();

// i18nのt関数を取得（翻訳用）
const { t } = useI18n();

// Todo完了状態の切り替え処理
// チェックボックスがクリックされた時に呼び出される
function toggleTodo() {
  // 親コンポーネントにtoggleイベントを発火
  emit('toggle', props.todo.id);
}

// Todo削除処理
// 削除ボタンがクリックされた時に呼び出される
function removeTodo() {
  // 親コンポーネントにremoveイベントを発火
  emit('remove', props.todo.id);
}
</script>

<template>
  <li :class="['todo-item', { 'completed': todo.completed }]">
    <div class="todo-content">
      <button 
        class="checkbox" 
        @click="toggleTodo"
        :aria-checked="todo.completed"
        :aria-label="todo.completed ? 'タスクを未完了にする' : 'タスクを完了にする'"
      >
        <svg v-if="todo.completed" xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="check-icon">
          <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path>
          <polyline points="22 4 12 14.01 9 11.01"></polyline>
        </svg>
        <svg v-else xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="circle-icon">
          <circle cx="12" cy="12" r="10"></circle>
        </svg>
      </button>
      <span class="todo-text">{{ todo.text }}</span>
      <span v-if="todo.category" class="category-badge">
        {{ t('app.categories.' + todo.category) }}
      </span>
    </div>
    <button 
      class="delete-button" 
      @click="removeTodo"
      aria-label="タスクを削除"
    >
      <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="x-icon">
        <line x1="18" y1="6" x2="6" y2="18"></line>
        <line x1="6" y1="6" x2="18" y2="18"></line>
      </svg>
    </button>
  </li>
</template>

<style scoped>
.todo-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0.75rem 1rem;
  margin-bottom: 0.5rem;
  border-radius: var(--radius);
  border: 1px solid var(--border);
  background-color: var(--card);
  transition: background-color 0.2s, border-color 0.2s;
}

.todo-item:hover {
  border-color: var(--input);
}

.todo-item.completed {
  background-color: var(--muted);
}

.todo-content {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  flex: 1;
}

.checkbox {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 1.5rem;
  height: 1.5rem;
  border-radius: 50%;
  background: transparent;
  border: none;
  cursor: pointer;
  padding: 0;
  color: var(--card-foreground);
}

.todo-item.completed .checkbox {
  color: var(--primary);
}

.todo-text {
  flex: 1;
  transition: color 0.2s;
}

.todo-item.completed .todo-text {
  text-decoration: line-through;
  color: var(--muted-foreground);
}

.category-badge {
  font-size: 0.75rem;
  padding: 0.25rem 0.5rem;
  border-radius: 9999px;
  background-color: var(--accent);
  color: var(--accent-foreground);
}

.delete-button {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 2rem;
  height: 2rem;
  border-radius: var(--radius);
  border: none;
  background-color: transparent;
  color: var(--muted-foreground);
  cursor: pointer;
  transition: background-color 0.2s, color 0.2s;
}

.delete-button:hover {
  background-color: rgba(239, 68, 68, 0.1);
  color: var(--destructive);
}

.check-icon, .circle-icon, .x-icon {
  display: inline-block;
  vertical-align: middle;
}
</style> 