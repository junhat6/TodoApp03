/**
 * API設定
 */

// APIベースURL（環境変数から取得、またはデフォルト値を使用）
export const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || "/api";

// APIリクエストのタイムアウト（ミリ秒）
export const API_TIMEOUT = 10000;

// API関連のエンドポイント
export const API_ENDPOINTS = {
  TODOS: `${API_BASE_URL}/todos`,
  TODO_BY_ID: (id: number) => `${API_BASE_URL}/todos/${id}`,
};

// API共通ヘッダー
export const DEFAULT_HEADERS = {
  "Content-Type": "application/json",
};
