import type { Todo, ApiResponse } from "../types/todo";
import { API_ENDPOINTS, API_TIMEOUT, DEFAULT_HEADERS } from "../config/api";
import { DEFAULT_CATEGORY } from "../constants/categories";

/**
 * APIリクエストをタイムアウト付きで実行する関数
 * ネットワークの問題でリクエストが永続的にハングするのを防ぐ
 */
const fetchWithTimeout = async (
  url: string,
  options: RequestInit = {}
): Promise<Response> => {
  // AbortController でリクエストのキャンセルを制御
  const controller = new AbortController();
  const { signal } = controller;

  // 指定時間後にリクエストを自動キャンセル
  const timeout = setTimeout(() => {
    controller.abort();
  }, API_TIMEOUT);

  try {
    // fetch APIでHTTPリクエストを実行
    const response = await fetch(url, { ...options, signal });
    clearTimeout(timeout); // 成功時はタイマーをクリア
    return response;
  } catch (error) {
    clearTimeout(timeout); // エラー時もタイマーをクリア
    // タイムアウトエラーの場合は分かりやすいメッセージに変換
    if ((error as Error).name === "AbortError") {
      throw new Error(`リクエストがタイムアウトしました (${API_TIMEOUT}ms)`);
    }
    throw error; // その他のエラーはそのまま再スロー
  }
};

/**
 * レスポンスを処理する共通関数
 * HTTPステータスコードのチェックとJSONパースを統一
 */
const handleResponse = async <T>(response: Response): Promise<T> => {
  // HTTPステータスコードが200番台以外の場合はエラー
  if (!response.ok) {
    const errorText = await response.text();
    try {
      // エラーレスポンスがJSONの場合はパースしてメッセージを取得
      const errorJson = JSON.parse(errorText);
      throw new Error(
        errorJson.message ||
          `API Error: ${response.status} ${response.statusText}`
      );
    } catch (e) {
      // JSONパースに失敗した場合はHTTPステータスをそのまま使用
      throw new Error(`API Error: ${response.status} ${response.statusText}`);
    }
  }

  // DELETEリクエストの場合は空のレスポンスを返す
  // 204 No Content ステータスの場合
  if (response.status === 204) {
    return {} as T;
  }

  // 正常なレスポンスの場合はJSONとしてパース
  return response.json() as Promise<T>;
};

/**
 * Todo APIサービス
 * サーバーとの通信を抽象化し、コンポーネントから直接fetch APIを使わないようにする
 * これにより、API仕様の変更時の影響を局所化できる
 */
export const TodoApi = {
  /**
   * すべてのTodoを取得
   * アプリケーション起動時やリフレッシュ時に呼び出される
   */
  async getAll(): Promise<Todo[]> {
    try {
      const response = await fetchWithTimeout(API_ENDPOINTS.TODOS, {
        method: "GET",
      });
      if (!response.ok) {
        throw new Error(`API Error: ${response.status} ${response.statusText}`);
      }
      return await response.json();
    } catch (error) {
      console.error("Todo取得エラー:", error);
      throw error; // 呼び出し元でエラーハンドリングできるよう再スロー
    }
  },

  /**
   * 新しいTodoを追加
   * @param text Todoのテキスト内容
   * @param category Todoのカテゴリ（デフォルト: 個人）
   */
  async add(text: string, category: string = DEFAULT_CATEGORY): Promise<Todo> {
    // クライアント側での入力検証
    if (!text.trim()) {
      throw new Error("タスク内容を入力してください");
    }

    try {
      const response = await fetchWithTimeout(API_ENDPOINTS.TODOS, {
        method: "POST",
        headers: DEFAULT_HEADERS, // Content-Type: application/json など
        body: JSON.stringify({ text, category }), // オブジェクトをJSON文字列に変換
      });
      if (!response.ok) {
        throw new Error(`API Error: ${response.status} ${response.statusText}`);
      }
      return await response.json();
    } catch (error) {
      console.error("Todo追加エラー:", error);
      throw error;
    }
  },

  /**
   * 指定IDのTodoを取得
   * @param id 対象TodoのID
   */
  async getById(id: number): Promise<Todo> {
    try {
      const response = await fetchWithTimeout(API_ENDPOINTS.TODO_BY_ID(id), {
        method: "GET",
      });
      if (!response.ok) {
        throw new Error(`API Error: ${response.status} ${response.statusText}`);
      }
      return await response.json();
    } catch (error) {
      console.error(`Todo取得エラー (ID: ${id}):`, error);
      throw error;
    }
  },

  /**
   * Todoの完了状態を切り替え
   * @param id 対象TodoのID
   */
  async toggle(id: number): Promise<Todo> {
    try {
      const todo = await this.getById(id);
      const endpoint = todo.completed
        ? `${API_ENDPOINTS.TODO_BY_ID(id)}/incomplete`
        : `${API_ENDPOINTS.TODO_BY_ID(id)}/complete`;

      const response = await fetchWithTimeout(endpoint, {
        method: "PATCH",
      });
      if (!response.ok) {
        throw new Error(`API Error: ${response.status} ${response.statusText}`);
      }
      return await response.json();
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
      if (!response.ok) {
        throw new Error(`API Error: ${response.status} ${response.statusText}`);
      }
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
      if (!response.ok) {
        throw new Error(`API Error: ${response.status} ${response.statusText}`);
      }
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
      if (!response.ok) {
        throw new Error(`API Error: ${response.status} ${response.statusText}`);
      }
    } catch (error) {
      console.error(`カテゴリー別削除エラー (カテゴリ: ${category}):`, error);
      throw error;
    }
  },
};
