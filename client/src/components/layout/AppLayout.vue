<script setup lang="ts">
import Sidebar from './Sidebar.vue';
import TodoView from '../../views/TodoView.vue';
import HabitsView from '../../views/HabitsView.vue';
import ProfileView from '../../views/ProfileView.vue';
import { useAppNavigation } from '../../composables/useAppNavigation';

// アプリナビゲーション機能を使用
const { currentPage, handlePageChange } = useAppNavigation();
</script>

<template>
  <div class="app-layout">
    <Sidebar 
      :currentPage="currentPage" 
      @change-page="handlePageChange"
    />
    
    <main class="main-content">
      <TodoView v-if="currentPage === 'todo'" />
      <HabitsView v-else-if="currentPage === 'habits'" />
      <ProfileView v-else-if="currentPage === 'profile'" />
    </main>
  </div>
</template>

<style scoped>
.app-layout {
  display: flex;
  min-height: 100vh;
}

.main-content {
  flex: 1;
  margin-left: 250px;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

/* モバイル対応 */
@media (max-width: 768px) {
  .app-layout {
    flex-direction: column;
  }
  
  .main-content {
    margin-left: 200px;
    min-height: calc(100vh - 60px);
  }
}

/* 小さなデバイス用 */
@media (max-width: 480px) {
  .app-layout {
    display: block;
  }
  
  .main-content {
    margin-left: 0;
    min-height: 100vh;
    padding-top: 60px;
  }
}
</style> 