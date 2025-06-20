<script setup lang="ts">
// Vue 3のComposition APIから必要な関数をインポート
// ref: リアクティブな変数を作成
// onMounted: コンポーネントがマウントされた時に実行
import { ref, onMounted } from 'vue';

// i18n（国際化）のコンポーザブル関数をインポート
import { useI18n } from 'vue-i18n';

// i18nの設定を取得
// t: 翻訳関数、locale: 現在の言語設定
const { t, locale } = useI18n();

// ダークモードの状態を管理するリアクティブ変数
const isDarkMode = ref(false);

// ダークモードの切り替え処理
function toggleDarkMode() {
  // 現在の状態を反転
  isDarkMode.value = !isDarkMode.value;
  
  // DOMのHTML要素にダークモードクラスを追加/削除
  // CSSのダークモードスタイルが適用される
  if (isDarkMode.value) {
    document.documentElement.classList.add('dark');
  } else {
    document.documentElement.classList.remove('dark');
  }
}

// システムのダークモード設定を確認（初期化時）
// ユーザーのOS設定に基づいて初期テーマを決定
onMounted(() => {
  // matchMediaでシステムのカラースキーム設定を確認
  if (window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches) {
    isDarkMode.value = true;
    document.documentElement.classList.add('dark');
  }
});

// 言語切り替え処理
// 日本語と英語を切り替える
function toggleLanguage() {
  locale.value = locale.value === 'ja' ? 'en' : 'ja';
}
</script>

<template>
  <div class="card-header">
    <div class="header-left">
      <h1>{{ t('app.title') }}</h1>
      <button @click="toggleLanguage" class="language-button">
        {{ locale === 'ja' ? 'EN' : 'JA' }}
      </button>
    </div>
    <button @click="toggleDarkMode" class="icon-button" :aria-label="t('app.darkMode')">
      <svg v-if="isDarkMode" xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="icon">
        <circle cx="12" cy="12" r="5"></circle>
        <line x1="12" y1="1" x2="12" y2="3"></line>
        <line x1="12" y1="21" x2="12" y2="23"></line>
        <line x1="4.22" y1="4.22" x2="5.64" y2="5.64"></line>
        <line x1="18.36" y1="18.36" x2="19.78" y2="19.78"></line>
        <line x1="1" y1="12" x2="3" y2="12"></line>
        <line x1="21" y1="12" x2="23" y2="12"></line>
        <line x1="4.22" y1="19.78" x2="5.64" y2="18.36"></line>
        <line x1="18.36" y1="5.64" x2="19.78" y2="4.22"></line>
      </svg>
      <svg v-else xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="icon">
        <path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z"></path>
      </svg>
    </button>
  </div>
</template>

<style scoped>
.header-left {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.language-button {
  background-color: var(--muted);
  color: var(--muted-foreground);
  border: none;
  border-radius: var(--radius);
  padding: 0.25rem 0.5rem;
  font-size: 0.75rem;
  font-weight: 600;
  cursor: pointer;
  transition: all var(--transition-fast);
}

.language-button:hover {
  background-color: var(--primary);
  color: white;
}
</style> 