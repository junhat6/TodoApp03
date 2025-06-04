/**
 * Todoアプリケーションで使用する型定義
 * TypeScriptの型安全性を活用してバグを防ぎ、開発効率を向上させる
 */

// Todoアイテムの型定義
// interfaceを使用してオブジェクトの構造を定義
export interface Todo {
  id: number;           // 一意識別子（サーバーで生成される）
  text: string;         // タスクの内容
  completed: boolean;   // 完了状態（true: 完了、false: 未完了）
  category: string;     // カテゴリー（work, personal, shopping, other）
}

// API関連の型定義
// ジェネリクス<T>を使用して、様々なデータ型に対応可能な汎用型
export interface ApiResponse<T> {
  data?: T;             // 成功時のデータ（オプショナル）
  error?: string;       // エラーメッセージ（オプショナル）
  status: number;       // HTTPステータスコード（必須）
}

// APIエラーの型定義
// エラーハンドリングを統一するための型
export interface ApiError {
  message: string;      // エラーメッセージ（必須）
  statusCode?: number;  // HTTPステータスコード（オプショナル）
}
