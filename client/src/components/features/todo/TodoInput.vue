<script setup lang="ts">
// Vue 3のComposition APIから必要な関数をインポート
// ref: リアクティブな変数を作成
// watch: リアクティブデータの変更を監視
// defineProps: プロパティの型定義
import { ref, watch, defineProps } from 'vue';

// i18n（国際化）のコンポーザブル関数をインポート
import { useI18n } from 'vue-i18n';

// Piniaストアをインポート
import { useTodoStore } from '../../../store/todo';

// カテゴリー関連の定数をインポート
import { CATEGORY_IDS, DEFAULT_CATEGORY, ALL_CATEGORIES } from '../../../constants/categories';
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

// 入力フィールドのテキストを管理するリアクティブ変数
const text = ref('');

// 新規タスクに設定するカテゴリーを管理するリアクティブ変数
const selectedCategory = ref<CategoryId>(DEFAULT_CATEGORY);

// カテゴリードロップダウンの表示状態を管理
const showCategoryDropdown = ref(false);

// カテゴリーの選択リスト（「すべて」を除外）
// 新規タスク作成時は具体的なカテゴリーのみ選択可能
const categoryOptions = [
  CATEGORY_IDS.WORK,      // 仕事
  CATEGORY_IDS.PERSONAL,  // 個人
  CATEGORY_IDS.SHOPPING,  // 買い物
  CATEGORY_IDS.OTHER      // その他
];

// 選択されたカテゴリータブが変更されたときに反映
// watch関数でプロパティの変更を監視し、自動的に入力フォームのカテゴリーを同期
watch(() => props.selectedCategory, (newCategory) => {
  // 「すべて」以外の有効なカテゴリーが選択された場合
  if (newCategory !== ALL_CATEGORIES && categoryOptions.includes(newCategory)) {
    selectedCategory.value = newCategory;
  }
}, { immediate: true }); // immediate: true で初期化時にも実行

// 新規タスク追加処理
function onAdd() {
  // 入力値をトリムして検証
  const v = text.value && text.value.trim();
  if (!v) return; // 空の場合は処理終了

  // カテゴリ情報を含めてTodoを追加
  // ストアのaddTodoアクションを呼び出し
  store.addTodo(v, selectedCategory.value);
  
  // 入力フィールドをクリア
  text.value = '';
}

// カテゴリードロップダウンの表示切り替え
function toggleCategoryDropdown() {
  showCategoryDropdown.value = !showCategoryDropdown.value;
}

// ドロップダウンからカテゴリーを選択
function selectCategory(category: CategoryId) {
  selectedCategory.value = category;
  showCategoryDropdown.value = false; // ドロップダウンを閉じる
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