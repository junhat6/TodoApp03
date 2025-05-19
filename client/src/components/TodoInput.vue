<script setup lang="ts">
import { ref, watch, defineProps } from 'vue';
import { useI18n } from 'vue-i18n';
import { useTodoStore } from '../store/todo';
import { CATEGORY_IDS, DEFAULT_CATEGORY, ALL_CATEGORIES } from '../constants/categories';
import type { CategoryId } from '../constants/categories';

const { t } = useI18n();

const props = defineProps({
  selectedCategory: {
    type: String as () => CategoryId,
    default: ALL_CATEGORIES
  }
});

const store = useTodoStore();
const text = ref('');
const selectedCategory = ref<CategoryId>(DEFAULT_CATEGORY);
const showCategoryDropdown = ref(false);

// カテゴリーの選択リスト（「すべて」を除外）
const categoryOptions = [
  CATEGORY_IDS.WORK,
  CATEGORY_IDS.PERSONAL, 
  CATEGORY_IDS.SHOPPING, 
  CATEGORY_IDS.OTHER
];

// 選択されたカテゴリータブが変更されたときに反映
watch(() => props.selectedCategory, (newCategory) => {
  if (newCategory !== ALL_CATEGORIES && categoryOptions.includes(newCategory)) {
    selectedCategory.value = newCategory;
  }
}, { immediate: true });

function onAdd() {
  const v = text.value && text.value.trim();
  if (!v) return;

  // カテゴリ情報を含めてTodoを追加
  store.addTodo(v, selectedCategory.value);
  text.value = '';
}

function toggleCategoryDropdown() {
  showCategoryDropdown.value = !showCategoryDropdown.value;
}

function selectCategory(category: CategoryId) {
  selectedCategory.value = category;
  showCategoryDropdown.value = false;
}
</script>

<template>
  <div class="todo-input">
    <div class="input-wrapper">
      <input
        v-model="text"
        @keyup.enter="onAdd"
        :placeholder="t('app.task.placeholder')"
        class="text-input"
      />
    </div>
    
    <div class="dropdown-container">
      <button @click="toggleCategoryDropdown" class="category-button">
        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="icon">
          <path d="M20.59 13.41l-7.17 7.17a2 2 0 0 1-2.83 0L2 12V2h10l8.59 8.59a2 2 0 0 1 0 2.82z"></path>
          <line x1="7" y1="7" x2="7.01" y2="7"></line>
        </svg>
        <span class="selected-category">{{ t(`app.categories.${selectedCategory}`) }}</span>
      </button>
      
      <div v-if="showCategoryDropdown" class="dropdown-menu">
        <button 
          v-for="category in categoryOptions" 
          :key="category"
          @click="selectCategory(category)"
          class="dropdown-item"
          :class="{ 'active': selectedCategory === category }"
        >
          {{ t(`app.categories.${category}`) }}
        </button>
      </div>
    </div>
    
    <button @click="onAdd" class="add-button">
      <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="icon">
        <circle cx="12" cy="12" r="10"></circle>
        <line x1="12" y1="8" x2="12" y2="16"></line>
        <line x1="8" y1="12" x2="16" y2="12"></line>
      </svg>
      {{ t('app.task.add') }}
    </button>
  </div>
</template>

<style scoped>
.todo-input {
  display: flex;
  gap: 0.5rem;
  width: 100%;
}

.input-wrapper {
  flex: 1;
  position: relative;
}

.text-input {
  width: 100%;
  padding: 0.75rem 1rem;
  border-radius: var(--radius);
  border: 1px solid var(--input);
  background-color: transparent;
  color: var(--card-foreground);
  font-size: 0.875rem;
  transition: border-color 0.2s;
}

.text-input:focus {
  outline: none;
  border-color: var(--ring);
  box-shadow: 0 0 0 2px rgba(99, 102, 241, 0.1);
}

.category-button {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.75rem;
  border-radius: var(--radius);
  border: 1px solid var(--input);
  background-color: transparent;
  color: var(--card-foreground);
  cursor: pointer;
  transition: border-color 0.2s, background-color 0.2s;
}

.category-button:hover {
  background-color: var(--accent);
}

.selected-category {
  font-size: 0.875rem;
}

.dropdown-container {
  position: relative;
}

.dropdown-menu {
  position: absolute;
  top: calc(100% + 0.25rem);
  right: 0;
  width: 150px;
  background-color: var(--card);
  border-radius: var(--radius);
  border: 1px solid var(--border);
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
  z-index: 10;
  overflow: hidden;
}

.dropdown-item {
  display: block;
  width: 100%;
  padding: 0.5rem 1rem;
  text-align: left;
  background: transparent;
  border: none;
  color: var(--card-foreground);
  cursor: pointer;
  transition: background-color 0.2s;
}

.dropdown-item:hover {
  background-color: var(--accent);
}

.dropdown-item.active {
  background-color: var(--accent);
  color: var(--primary);
}

.add-button {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.75rem 1rem;
  border-radius: var(--radius);
  border: none;
  background-color: var(--primary);
  color: white;
  font-size: 0.875rem;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.2s;
}

.add-button:hover {
  background-color: var(--primary-hover);
}

.icon {
  display: inline-block;
  vertical-align: middle;
}
</style>