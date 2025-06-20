import { ref } from 'vue';
import type { PageType } from '../types/navigation';

/**
 * アプリケーションのナビゲーション状態を管理するComposable
 * @returns ナビゲーション状態と操作関数
 */
export function useAppNavigation() {
  // 現在のページ状態を管理
  const currentPage = ref<PageType>('todo');
  
  // ページ変更ハンドラー
  const handlePageChange = (page: PageType) => {
    currentPage.value = page;
  };
  
  // 現在のページが指定されたページかどうかを判定
  const isCurrentPage = (page: PageType) => {
    return currentPage.value === page;
  };
  
  // 前のページに戻る（履歴機能用）
  const goBack = () => {
    // 将来的にページ履歴機能を実装する際に使用
    console.log('Going back...');
  };
  
  return {
    currentPage,
    handlePageChange,
    isCurrentPage,
    goBack
  };
} 