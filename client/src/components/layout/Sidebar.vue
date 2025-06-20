<script setup lang="ts">
import { useI18n } from 'vue-i18n';

// 型定義のインポート
import type { PageType } from '../../types/navigation';
export type { PageType } from '../../types/navigation';

interface Props {
  currentPage: PageType;
}

// Emitのイベント定義
interface Emits {
  (e: 'change-page', page: PageType): void;
}

const props = defineProps<Props>();
const emit = defineEmits<Emits>();

// i18nのt関数を取得
const { t } = useI18n();

// ページ変更ハンドラー
function handlePageChange(page: PageType) {
  emit('change-page', page);
}
</script>

<template>
  <aside class="sidebar">
    <nav class="sidebar-nav">
      <h2 class="sidebar-title">{{ t('app.title') }}</h2>
      
      <ul class="nav-menu">
        <li class="nav-item">
          <button
            @click="handlePageChange('todo')"
            :class="['nav-button', { 'active': currentPage === 'todo' }]"
          >
            <svg class="nav-icon" viewBox="0 0 20 20" fill="currentColor">
              <path d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"/>
            </svg>
            {{ t('app.sidebar.todo') }}
          </button>
        </li>
        
        <li class="nav-item">
          <button
            @click="handlePageChange('habits')"
            :class="['nav-button', { 'active': currentPage === 'habits' }]"
          >
            <svg class="nav-icon" viewBox="0 0 20 20" fill="currentColor">
              <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z"/>
            </svg>
            {{ t('app.sidebar.habits') }}
          </button>
        </li>
      </ul>
    </nav>
  </aside>
</template>

<style scoped>
.sidebar {
  position: fixed;
  top: 0;
  left: 0;
  width: 250px;
  background-color: var(--card);
  border-right: 1px solid var(--border);
  height: 100vh;
  display: flex;
  flex-direction: column;
  transition: background-color 0.3s;
  z-index: 1000;
}

.sidebar-nav {
  padding: 1.5rem;
  height: 100%;
}

.sidebar-title {
  font-size: 1.25rem;
  font-weight: 600;
  color: var(--card-foreground);
  margin-bottom: 2rem;
  text-align: center;
}

.nav-menu {
  list-style: none;
  margin: 0;
  padding: 0;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.nav-item {
  margin: 0;
}

.nav-button {
  width: 100%;
  display: flex;
  align-items: center;
  gap: 0.75rem;
  padding: 0.75rem 1rem;
  background: transparent;
  border: none;
  border-radius: var(--radius);
  color: var(--muted-foreground);
  cursor: pointer;
  font-size: 0.875rem;
  font-weight: 500;
  text-align: left;
  transition: all 0.2s;
}

.nav-button:hover {
  background-color: var(--accent);
  color: var(--accent-foreground);
}

.nav-button.active {
  background-color: var(--primary);
  color: white;
}

.nav-icon {
  width: 20px;
  height: 20px;
  flex-shrink: 0;
}

/* モバイル対応 */
@media (max-width: 768px) {
  .sidebar {
    width: 200px;
  }
  
  .sidebar-nav {
    padding: 1rem;
  }
  
  .sidebar-title {
    font-size: 1.1rem;
    margin-bottom: 1.5rem;
  }
  
  .nav-button {
    padding: 0.5rem 0.75rem;
    font-size: 0.8rem;
  }
  
  .nav-icon {
    width: 18px;
    height: 18px;
  }
}

/* 小さなデバイス用 */
@media (max-width: 480px) {
  .sidebar {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    width: 100%;
    height: 60px;
    flex-direction: row;
    z-index: 1001;
  }
  
  .sidebar-nav {
    padding: 0.5rem 1rem;
    display: flex;
    align-items: center;
    justify-content: space-between;
    width: 100%;
  }
  
  .sidebar-title {
    font-size: 1rem;
    margin-bottom: 0;
    margin-right: 1rem;
  }
  
  .nav-menu {
    flex-direction: row;
    gap: 0;
  }
  
  .nav-button {
    padding: 0.5rem;
    font-size: 0.75rem;
    min-width: auto;
  }
  
  .nav-icon {
    width: 16px;
    height: 16px;
  }
}
</style> 