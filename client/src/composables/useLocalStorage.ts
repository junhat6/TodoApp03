import { ref, watch, type Ref } from 'vue';

/**
 * LocalStorageと同期するリアクティブな値を提供するComposable
 * @param key LocalStorageのkey
 * @param defaultValue デフォルト値
 * @returns リアクティブな値
 */
export function useLocalStorage<T>(
  key: string,
  defaultValue: T
) {
  // LocalStorageから初期値を読み込み
  const storedValue = localStorage.getItem(key);
  const initialValue = storedValue ? JSON.parse(storedValue) : defaultValue;
  
  // リアクティブな状態を作成
  const state = ref<T>(initialValue);
  
  // 値を設定する関数
  const setValue = (value: T) => {
    state.value = value;
    localStorage.setItem(key, JSON.stringify(value));
  };
  
  // 状態の変更を監視してLocalStorageに保存
  watch(
    state,
    (newValue) => {
      localStorage.setItem(key, JSON.stringify(newValue));
    },
    { deep: true }
  );
  
  return { state, setValue };
}

/**
 * ダークモードの状態を管理するComposable
 * @returns ダークモードの状態と切り替え関数
 */
export function useDarkMode() {
  const { state: isDark, setValue: setIsDark } = useLocalStorage('dark-mode', false);
  
  const toggleDarkMode = () => {
    setIsDark(!isDark.value);
    
    // HTMLのクラスを切り替え
    if (isDark.value) {
      document.documentElement.classList.add('dark');
    } else {
      document.documentElement.classList.remove('dark');
    }
  };
  
  // 初期化時にクラスを設定
  if (isDark.value) {
    document.documentElement.classList.add('dark');
  }
  
  return {
    isDark,
    toggleDarkMode
  };
} 