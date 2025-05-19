import type { Todo, ApiResponse } from "../types/todo";
import { API_ENDPOINTS, API_TIMEOUT, DEFAULT_HEADERS } from "../config/api";
import { DEFAULT_CATEGORY } from "../constants/categories";

/**
 * APIリクエストをタイムアウト付きで実行する関数
 */
const fetchWithTimeout = async (
  url: string,
  options: RequestInit = {}
): Promise<Response> => {
  const controller = new AbortController();
  const { signal } = controller;

  const timeout = setTimeout(() => {
    controller.abort();
  }, API_TIMEOUT);

  try {
    const response = await fetch(url, { ...options, signal });
    clearTimeout(timeout);
    return response;
  } catch (error) {
    clearTimeout(timeout);
    if ((error as Error).name === "AbortError") {
      throw new Error(`リクエストがタイムアウトしました (${API_TIMEOUT}ms)`);
    }
    throw error;
  }
};

/**
 * レスポンスを処理する共通関数
 */
const handleResponse = async <T>(response: Response): Promise<T> => {
  if (!response.ok) {
    const errorText = await response.text();
    try {
      const errorJson = JSON.parse(errorText);
      throw new Error(
        errorJson.message ||
          `API Error: ${response.status} ${response.statusText}`
      );
    } catch (e) {
      throw new Error(`API Error: ${response.status} ${response.statusText}`);
    }
  }

  return response.json() as Promise<T>;
};

/**
 * Todo APIサービス
 * サーバーとの通信を抽象化します
 */
export const TodoApi = {
  /**
   * すべてのTodoを取得
   */
  async getAll(): Promise<Todo[]> {
    try {
      const response = await fetchWithTimeout(API_ENDPOINTS.TODOS, {
        method: "GET",
      });
      return await handleResponse<Todo[]>(response);
    } catch (error) {
      console.error("Todo取得エラー:", error);
      throw error;
    }
  },

  /**
   * 新しいTodoを追加
   * @param text Todoのテキスト
   * @param category Todoのカテゴリ（デフォルト: 個人）
   */
  async add(text: string, category: string = DEFAULT_CATEGORY): Promise<Todo> {
    if (!text.trim()) {
      throw new Error("タスク内容を入力してください");
    }

    try {
      const response = await fetchWithTimeout(API_ENDPOINTS.TODOS, {
        method: "POST",
        headers: DEFAULT_HEADERS,
        body: JSON.stringify({ text, category }),
      });
      return await handleResponse<Todo>(response);
    } catch (error) {
      console.error("Todo追加エラー:", error);
      throw error;
    }
  },

  /**
   * Todoの完了状態を切り替え
   * @param id 対象TodoのID
   */
  async toggle(id: number): Promise<Todo> {
    try {
      const response = await fetchWithTimeout(API_ENDPOINTS.TODO_BY_ID(id), {
        method: "PUT",
      });
      return await handleResponse<Todo>(response);
    } catch (error) {
      console.error(`Todo状態変更エラー (ID: ${id}):`, error);
      throw error;
    }
  },

  /**
   * 指定IDのTodoを削除
   * @param id 削除対象TodoのID
   */
  async remove(id: number): Promise<void> {
    try {
      const response = await fetchWithTimeout(API_ENDPOINTS.TODO_BY_ID(id), {
        method: "DELETE",
      });
      await handleResponse<void>(response);
    } catch (error) {
      console.error(`Todo削除エラー (ID: ${id}):`, error);
      throw error;
    }
  },

  /**
   * すべてのTodoを削除
   */
  async removeAll(): Promise<void> {
    try {
      const response = await fetchWithTimeout(API_ENDPOINTS.TODOS, {
        method: "DELETE",
      });
      await handleResponse<void>(response);
    } catch (error) {
      console.error("全Todo削除エラー:", error);
      throw error;
    }
  },

  /**
   * カテゴリー別にTodoを削除
   * @param category 削除対象カテゴリー
   */
  async removeByCategory(category: string): Promise<void> {
    if (!category || category === "すべて") {
      throw new Error("有効なカテゴリーを指定してください");
    }

    try {
      const response = await fetchWithTimeout(
        `${API_ENDPOINTS.TODOS}/category/${encodeURIComponent(category)}`,
        { method: "DELETE" }
      );
      await handleResponse<void>(response);
    } catch (error) {
      console.error(`カテゴリー別削除エラー (カテゴリ: ${category}):`, error);
      throw error;
    }
  },
};
