<script setup lang="ts">
import { useI18n } from 'vue-i18n';

// i18nのt関数を取得
const { t } = useI18n();
</script>

<template>
  <div class="habits-view">
    <div class="habits-container">
      <div class="card">
        <div class="card-header">
          <h1>{{ t('app.sidebar.habits') }}</h1>
        </div>
        
        <div class="card-content">
          <div class="placeholder-content">
            <div class="placeholder-icon">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z"/>
              </svg>
            </div>
            <h2 class="placeholder-title">習慣トラッカー</h2>
            <p class="placeholder-description">
              この画面は現在開発中です。<br>
              習慣の追加・管理機能を実装予定です。
            </p>
            <div class="coming-soon">
              <span class="badge">Coming Soon</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.habits-view {
  flex: 1;
  padding: 2rem;
  background-color: var(--background);
  overflow-y: auto;
}

.habits-container {
  max-width: 600px;
  margin: 0 auto;
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
}

.card-header h1 {
  font-size: 1.5rem;
  font-weight: 600;
  color: var(--card-foreground);
  margin: 0;
}

.card-content {
  padding: 3rem 1.5rem;
}

.placeholder-content {
  text-align: center;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1.5rem;
}

.placeholder-icon {
  width: 80px;
  height: 80px;
  color: var(--muted-foreground);
  opacity: 0.5;
}

.placeholder-icon svg {
  width: 100%;
  height: 100%;
}

.placeholder-title {
  font-size: 1.5rem;
  font-weight: 600;
  color: var(--card-foreground);
  margin: 0;
}

.placeholder-description {
  color: var(--muted-foreground);
  font-size: 1rem;
  line-height: 1.6;
  max-width: 400px;
  margin: 0;
}

.coming-soon {
  margin-top: 1rem;
}

.badge {
  display: inline-block;
  padding: 0.5rem 1rem;
  background-color: var(--primary);
  color: white;
  border-radius: 9999px;
  font-size: 0.875rem;
  font-weight: 500;
}

/* モバイル対応 */
@media (max-width: 768px) {
  .habits-view {
    padding: 1rem;
  }
  
  .card-header {
    padding: 1rem;
  }
  
  .card-content {
    padding: 2rem 1rem;
  }
  
  .placeholder-icon {
    width: 60px;
    height: 60px;
  }
  
  .placeholder-title {
    font-size: 1.25rem;
  }
  
  .placeholder-description {
    font-size: 0.875rem;
  }
}
</style> 