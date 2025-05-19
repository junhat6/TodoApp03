<script setup lang="ts">
import { ref, computed } from 'vue';
import { useI18n } from 'vue-i18n';
import TodoInput from './components/TodoInput.vue';
import TodoList from './components/TodoList.vue';
import DeleteAllButton from './components/DeleteAllButton.vue';
import AppHeader from './components/AppHeader.vue';
import { useTodoStore } from './store/todo';
import { CATEGORIES, ALL_CATEGORIES } from './constants/categories';
import type { CategoryId } from './constants/categories';

const { t } = useI18n();
const store = useTodoStore();
const selectedCategory = ref<CategoryId>(ALL_CATEGORIES);

// カテゴリ別の残りタスク数を計算
const categoryRemaining = computed(() => {
  return store.getRemainingByCategory(selectedCategory.value);
});

// すべてのタスクの残り数
const totalRemaining = computed(() => store.remaining);

// カテゴリタブの選択
function selectCategory(category: CategoryId) {
  selectedCategory.value = category;
}
</script>

<template>
  <div class="app-wrapper">
    <div class="app-container">
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

<style>
:root {
  --primary: #6366f1;
  --primary-hover: #4f46e5;
  --destructive: #ef4444;
  --destructive-hover: #dc2626;
  --background: #ffffff;
  --card: #ffffff;
  --card-foreground: #0f172a;
  --border: #e2e8f0;
  --input: #e2e8f0;
  --ring: #6366f1;
  --muted: #f1f5f9;
  --muted-foreground: #64748b;
  --accent: #f1f5f9;
  --accent-foreground: #0f172a;
  --radius: 0.5rem;
}

.dark {
  --background: #0f172a;
  --card: #1e293b;
  --card-foreground: #f8fafc;
  --border: #334155;
  --input: #334155;
  --muted: #1e293b;
  --muted-foreground: #94a3b8;
  --accent: #1e293b;
  --accent-foreground: #f8fafc;
}

* {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif;
  line-height: 1.5;
  -webkit-font-smoothing: antialiased;
}

.app-wrapper {
  min-height: 100vh;
  background-color: var(--background);
  color: var(--card-foreground);
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 2rem 1rem;
  transition: background-color 0.3s, color 0.3s;
}

.app-container {
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

.card-header {
  padding: 1.5rem;
  border-bottom: 1px solid var(--border);
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header h1 {
  font-size: 1.5rem;
  font-weight: 600;
  margin: 0;
}

.icon-button {
  background: transparent;
  border: none;
  border-radius: var(--radius);
  color: var(--card-foreground);
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0.5rem;
  transition: background-color 0.2s;
}

.icon-button:hover {
  background-color: var(--accent);
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

.icon {
  display: inline-block;
  vertical-align: middle;
}

button {
  font-family: inherit;
}
</style>
