<script setup lang="ts">
// Vue 3のComposition APIから必要な関数をインポート
// ref: リアクティブな変数を作成
// watch: リアクティブデータの変更を監視
// computed: 算出プロパティを作成
// defineProps: プロパティの型定義
import { ref, watch, computed, defineProps } from 'vue';

// i18n（国際化）のコンポーザブル関数をインポート
import { useI18n } from 'vue-i18n';

// Piniaストアをインポート
import { useTodoStore } from '../../../store/todo';

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

// 確認ダイアログの表示状態を管理
const showConfirmDialog = ref(false);

// カテゴリーに応じたタスクをフィルタリングする算出プロパティ
// 選択されたカテゴリーに基づいて対象となるタスクを決定
const filteredTodos = computed(() => {
  if (props.selectedCategory === ALL_CATEGORIES) {
    // 「すべて」が選択されている場合は全タスクを対象
    return store.todos;
  }
  // 特定カテゴリーが選択されている場合はそのカテゴリーのタスクのみ
  return store.todos.filter(todo => todo.category === props.selectedCategory);
});

// 削除ボタンの表示制御のための算出プロパティ
// タスクが存在する場合のみボタンを表示
const hasTodos = computed(() => filteredTodos.value.length > 0);

// 削除するタスクがあるか監視
// deep: true でオブジェクト内部の変更も監視
watch(() => store.todos, () => {
  // Todoリストが変更されたときに計算プロパティが自動更新される
  // このwatchは明示的な処理は不要だが、デバッグ時に有用
}, { deep: true });

// 一括削除の確認ダイアログを表示
function confirmDeleteAll() {
  if (!hasTodos.value) return; // タスクがない場合は何もしない
  showConfirmDialog.value = true;
}

// 一括削除の実行処理
function deleteAllTodos() {
  if (props.selectedCategory === ALL_CATEGORIES) {
    // すべてのタスクを削除
    store.removeAllTodos();
  } else {
    // 特定カテゴリのタスクのみ削除
    store.removeTodosByCategory(props.selectedCategory);
  }
  // 確認ダイアログを閉じる
  showConfirmDialog.value = false;
}

// 確認ダイアログのキャンセル処理
function cancelDelete() {
  showConfirmDialog.value = false;
}
</script>

<template>
  <div class="delete-all-container">
    <button 
      v-if="hasTodos" 
      @click="confirmDeleteAll" 
      class="delete-all-btn"
    >
      <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="trash-icon">
        <polyline points="3 6 5 6 21 6"></polyline>
        <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path>
        <line x1="10" y1="11" x2="10" y2="17"></line>
        <line x1="14" y1="11" x2="14" y2="17"></line>
      </svg>
      {{ selectedCategory === ALL_CATEGORIES 
        ? t('app.action.deleteAll') 
        : t('app.action.deleteCategory', { category: t(`app.categories.${selectedCategory}`) }) 
      }}
    </button>
    
    <!-- 確認ダイアログ -->
    <div v-if="showConfirmDialog" class="confirm-dialog-overlay">
      <div class="confirm-dialog">
        <h3 class="dialog-title">{{ t('app.action.confirm') }}</h3>
        <p v-if="selectedCategory === ALL_CATEGORIES">
          {{ t('app.action.confirm') }}
        </p>
        <p v-else>
          {{ t('app.action.confirm') }}
        </p>
        <div class="dialog-buttons">
          <button @click="cancelDelete" class="cancel-btn">{{ t('app.action.cancel') }}</button>
          <button @click="deleteAllTodos" class="confirm-btn">{{ t('app.action.ok') }}</button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.delete-all-container {
  display: inline-block;
}

.delete-all-btn {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 1rem;
  border-radius: var(--radius);
  border: 1px solid var(--destructive);
  background-color: transparent;
  color: var(--destructive);
  font-size: 0.875rem;
  cursor: pointer;
  transition: background-color 0.2s, color 0.2s;
}

.delete-all-btn:hover {
  background-color: var(--destructive);
  color: white;
}

/* 確認ダイアログのスタイル */
.confirm-dialog-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 100;
}

.confirm-dialog {
  background: var(--card);
  color: var(--card-foreground);
  padding: 2rem;
  border-radius: var(--radius);
  max-width: 400px;
  width: 90%;
  box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05);
}

.dialog-title {
  margin-bottom: 1rem;
  font-size: 1.25rem;
  font-weight: 600;
}

.dialog-buttons {
  display: flex;
  justify-content: flex-end;
  gap: 0.75rem;
  margin-top: 1.5rem;
}

.cancel-btn {
  padding: 0.5rem 1rem;
  border-radius: var(--radius);
  border: 1px solid var(--border);
  background-color: transparent;
  color: var(--card-foreground);
  font-size: 0.875rem;
  cursor: pointer;
  transition: background-color 0.2s;
}

.confirm-btn {
  padding: 0.5rem 1rem;
  border-radius: var(--radius);
  border: none;
  background-color: var(--destructive);
  color: white;
  font-size: 0.875rem;
  cursor: pointer;
  transition: background-color 0.2s;
}

.cancel-btn:hover {
  background-color: var(--accent);
}

.confirm-btn:hover {
  background-color: var(--destructive-hover);
}

.trash-icon {
  display: inline-block;
  vertical-align: middle;
}
</style> 