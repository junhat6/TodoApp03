<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { profileStore } from '../store/profile';

const { t } = useI18n();
const isEditing = ref(false);
const editUsername = ref('');

// プロフィール初期化
onMounted(async () => {
  await profileStore.initializeProfile();
});

// 編集開始
function startEdit() {
  if (profileStore.profile.value) {
    editUsername.value = profileStore.profile.value.username;
    isEditing.value = true;
  }
}

// 編集キャンセル
function cancelEdit() {
  isEditing.value = false;
  editUsername.value = '';
}

// 保存
async function saveProfile() {
  if (editUsername.value.trim()) {
    await profileStore.updateProfile({ username: editUsername.value.trim() });
    isEditing.value = false;
  }
}

// 日付フォーマット
function formatDate(date: Date | string) {
  try {
    // 文字列の場合はDateオブジェクトに変換
    const dateObj = typeof date === 'string' ? new Date(date) : date;
    
    // 無効な日付をチェック
    if (isNaN(dateObj.getTime())) {
      return '日付不明';
    }
    
    return new Intl.DateTimeFormat('ja-JP', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    }).format(dateObj);
  } catch (error) {
    console.error('日付フォーマットエラー:', error);
    return '日付不明';
  }
}
</script>

<template>
  <div class="profile-view">
    <div class="profile-container">
      <h1 class="profile-title">{{ t('app.profile.title') }}</h1>
      
      <div v-if="profileStore.loading.value" class="loading">
        {{ t('app.profile.loading') }}
      </div>
      
      <div v-else-if="profileStore.error.value" class="error">
        {{ t('app.profile.error') }}: {{ profileStore.error.value }}
      </div>
      
      <div v-else-if="profileStore.profile.value" class="profile-content">
        <div class="profile-card">
          <div class="profile-avatar">
            <svg class="avatar-icon" viewBox="0 0 24 24" fill="currentColor">
              <path d="M12 2C13.1 2 14 2.9 14 4C14 5.1 13.1 6 12 6C10.9 6 10 5.1 10 4C10 2.9 10.9 2 12 2ZM21 9V7L15 7V9L15 7H13V21H11V16H10V21H8V7H6V9L0 9V7L6 7C6 7 10 7 10 7V4C10 2.9 10.9 2 12 2S14 2.9 14 4V7H18C18 7 22 7 22 7L21 9Z"/>
            </svg>
          </div>
          
          <div class="profile-info">
            <div class="profile-field">
              <label class="field-label">{{ t('app.profile.username') }}</label>
              <div v-if="!isEditing" class="field-value">
                <span>{{ profileStore.profile.value.username }}</span>
                <button @click="startEdit" class="edit-button">
                  {{ t('app.profile.edit') }}
                </button>
              </div>
              <div v-else class="field-edit">
                <input 
                  v-model="editUsername" 
                  type="text" 
                  class="username-input"
                  @keyup.enter="saveProfile"
                  @keyup.escape="cancelEdit"
                />
                <div class="edit-actions">
                  <button @click="saveProfile" class="save-button">
                    {{ t('app.profile.save') }}
                  </button>
                  <button @click="cancelEdit" class="cancel-button">
                    {{ t('app.profile.cancel') }}
                  </button>
                </div>
              </div>
            </div>
            
            <div class="profile-field">
              <label class="field-label">{{ t('app.profile.level') }}</label>
              <div class="field-value">
                <div class="level-display">
                  <span class="level-number">{{ profileStore.profile.value.level }}</span>
                  <div class="level-bar">
                    <div class="level-progress" :style="{ width: `${(profileStore.profile.value.level % 10) * 10}%` }"></div>
                  </div>
                </div>
              </div>
            </div>
            
            <div class="profile-field">
              <label class="field-label">{{ t('app.profile.joinDate') }}</label>
              <div class="field-value">
                {{ formatDate(profileStore.profile.value.createdAt) }}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.profile-view {
  padding: 2rem;
  max-width: 800px;
  margin: 0 auto;
}

.profile-container {
  background-color: var(--card);
  border: 1px solid var(--border);
  border-radius: var(--radius);
  padding: 2rem;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.profile-title {
  font-size: 2rem;
  font-weight: 700;
  color: var(--card-foreground);
  margin-bottom: 2rem;
  text-align: center;
}

.loading, .error {
  text-align: center;
  padding: 2rem;
  color: var(--muted-foreground);
}

.error {
  color: var(--destructive);
}

.profile-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 2rem;
}

.profile-avatar {
  width: 120px;
  height: 120px;
  background: linear-gradient(135deg, var(--primary), var(--accent));
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

.avatar-icon {
  width: 60px;
  height: 60px;
}

.profile-info {
  width: 100%;
  max-width: 400px;
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.profile-field {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.field-label {
  font-weight: 600;
  color: var(--card-foreground);
  font-size: 0.875rem;
}

.field-value {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0.75rem;
  background-color: var(--muted);
  border-radius: var(--radius);
  color: var(--muted-foreground);
}

.field-edit {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.username-input {
  padding: 0.75rem;
  border: 1px solid var(--border);
  border-radius: var(--radius);
  background-color: var(--background);
  color: var(--foreground);
  font-size: 1rem;
}

.username-input:focus {
  outline: none;
  border-color: var(--primary);
  box-shadow: 0 0 0 2px rgba(var(--primary-rgb), 0.2);
}

.edit-button, .save-button, .cancel-button {
  padding: 0.5rem 1rem;
  border: none;
  border-radius: var(--radius);
  font-size: 0.875rem;
  cursor: pointer;
  transition: all 0.2s;
}

.edit-button {
  background-color: var(--primary);
  color: white;
}

.edit-button:hover {
  background-color: var(--primary-hover, var(--primary));
  opacity: 0.9;
}

.edit-actions {
  display: flex;
  gap: 0.5rem;
}

.save-button {
  background-color: var(--primary);
  color: white;
  flex: 1;
}

.cancel-button {
  background-color: var(--muted);
  color: var(--muted-foreground);
  flex: 1;
}

.save-button:hover {
  opacity: 0.9;
}

.cancel-button:hover {
  background-color: var(--destructive);
  color: white;
}

.level-display {
  display: flex;
  align-items: center;
  gap: 1rem;
  flex: 1;
}

.level-number {
  font-size: 1.5rem;
  font-weight: 700;
  color: var(--primary);
}

.level-bar {
  flex: 1;
  height: 8px;
  background-color: var(--muted);
  border-radius: 4px;
  overflow: hidden;
}

.level-progress {
  height: 100%;
  background: linear-gradient(90deg, var(--primary), var(--accent));
  transition: width 0.3s ease;
}

/* モバイル対応 */
@media (max-width: 768px) {
  .profile-view {
    padding: 1rem;
  }
  
  .profile-container {
    padding: 1.5rem;
  }
  
  .profile-title {
    font-size: 1.5rem;
    margin-bottom: 1.5rem;
  }
  
  .profile-avatar {
    width: 100px;
    height: 100px;
  }
  
  .avatar-icon {
    width: 50px;
    height: 50px;
  }
  
  .profile-info {
    max-width: 100%;
  }
  
  .edit-actions {
    flex-direction: column;
  }
}
</style> 