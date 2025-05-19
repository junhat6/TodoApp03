/**
 * Todoアプリケーションで使用する型定義
 */

// Todoアイテムの型
export interface Todo {
  id: number;
  text: string;
  completed: boolean;
  category: string;
}

// API関連の型定義
export interface ApiResponse<T> {
  data?: T;
  error?: string;
  status: number;
}

// APIエラーの型定義
export interface ApiError {
  message: string;
  statusCode?: number;
}
