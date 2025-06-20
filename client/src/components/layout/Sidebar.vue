<script setup lang="ts">
import { useI18n } from 'vue-i18n';
import { onMounted } from 'vue';
import { profileStore } from '../../store/profile';

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

// プロフィール初期化
onMounted(async () => {
  await profileStore.initializeProfile();
});

// ページ変更ハンドラー
function handlePageChange(page: PageType) {
  emit('change-page', page);
}
</script>

<template>
  <aside class="sidebar">
    <nav class="sidebar-nav">
      <h2 class="sidebar-title">{{ t('app.title') }}</h2>
      
      <!-- プロフィール情報セクション -->
      <div v-if="profileStore.profile.value" class="profile-section">
        <div class="profile-card">
          <div class="profile-avatar">
            <svg class="avatar-icon" viewBox="0 0 24 24" fill="currentColor">
              <path d="M12 2C13.1 2 14 2.9 14 4C14 5.1 13.1 6 12 6C10.9 6 10 5.1 10 4C10 2.9 10.9 2 12 2Z"/>
            </svg>
          </div>
          <div class="profile-info">
            <span class="profile-username">{{ profileStore.profile.value.username }}</span>
            <span class="profile-level">レベル {{ profileStore.profile.value.level }}</span>
          </div>
        </div>
      </div>
      
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
        
        <li class="nav-item">
          <button
            @click="handlePageChange('profile')"
            :class="['nav-button', { 'active': currentPage === 'profile' }]"
          >
            <svg class="nav-icon" viewBox="0 0 20 20" fill="currentColor">
              <path d="M10 2C11.1 2 12 2.9 12 4C12 5.1 11.1 6 10 6C8.9 6 8 5.1 8 4C8 2.9 8.9 2 10 2ZM15 8C15.6 8 16 8.4 16 9V19C16 19.6 15.6 20 15 20H5C4.4 20 4 19.6 4 19V9C4 8.4 4.4 8 5 8H15Z"/>
            </svg>
            {{ t('app.sidebar.profile') }}
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
  display: flex;
  flex-direction: column;
}

.sidebar-title {
  font-size: 1.25rem;
  font-weight: 600;
  color: var(--card-foreground);
  margin-bottom: 1.5rem;
  text-align: center;
}

.profile-section {
  margin-bottom: 1.5rem;
  padding-bottom: 1.5rem;
  border-bottom: 1px solid var(--border);
}

.profile-card {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  padding: 0.75rem;
  background-color: var(--muted);
  border-radius: var(--radius);
}

.profile-avatar {
  width: 40px;
  height: 40px;
  background: linear-gradient(135deg, var(--primary), var(--accent));
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  flex-shrink: 0;
}

.avatar-icon {
  width: 20px;
  height: 20px;
}

.profile-info {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
  min-width: 0;
  flex: 1;
}

.profile-username {
  font-weight: 600;
  color: var(--card-foreground);
  font-size: 0.875rem;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.profile-level {
  font-size: 0.75rem;
  color: var(--muted-foreground);
}

.nav-menu {
  list-style: none;
  margin: 0;
  padding: 0;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  flex: 1;
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
    margin-bottom: 1rem;
  }
  
  .profile-section {
    margin-bottom: 1rem;
    padding-bottom: 1rem;
  }
  
  .profile-card {
    padding: 0.5rem;
    gap: 0.5rem;
  }
  
  .profile-avatar {
    width: 35px;
    height: 35px;
  }
  
  .avatar-icon {
    width: 18px;
    height: 18px;
  }
  
  .profile-username {
    font-size: 0.8rem;
  }
  
  .profile-level {
    font-size: 0.7rem;
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
    flex-direction: row;
  }
  
  .sidebar-title {
    font-size: 1rem;
    margin-bottom: 0;
    margin-right: 1rem;
  }
  
  .profile-section {
    display: none;
  }
  
  .nav-menu {
    flex-direction: row;
    gap: 0;
    flex: 1;
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