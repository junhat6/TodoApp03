import { defineStore } from "pinia";
import { TodoApi } from "../api/todo";
import type { Todo } from "../types/todo";
import { ALL_CATEGORIES } from "../constants/categories";

interface TodoState {
  todos: Todo[];
  nextId: number;
  loading: boolean;
  error: string | null;
}

/**
 * Todoの状態管理ストア
 */
export const useTodoStore = defineStore("todo", {
  state: (): TodoState => ({
    todos: [],
    nextId: 1,
    loading: false,
    error: null,
  }),
  actions: {
    // エラーハンドリングユーティリティ
    setError(message: string) {
      this.error = message;
      console.error(message);

      // 3秒後にエラーをクリア
      setTimeout(() => {
        this.clearError();
      }, 3000);
    },

    clearError() {
      this.error = null;
    },

    // 読み込み中の状態を設定
    setLoading(isLoading: boolean) {
      this.loading = isLoading;
    },

    /**
     * 全Todoを読み込む
     */
    async loadTodo() {
      this.setLoading(true);
      this.clearError();

      try {
        const list = await TodoApi.getAll();
        this.todos = list;
      } catch (error) {
        this.setError(
          `Todo読み込みエラー: ${
            error instanceof Error ? error.message : String(error)
          }`
        );
      } finally {
        this.setLoading(false);
      }
    },

    /**
     * 新しいTodoを追加
     */
    async addTodo(text: string, category: string) {
      if (!text.trim()) {
        this.setError("タスク内容を入力してください");
        return;
      }

      this.setLoading(true);
      this.clearError();

      try {
        const todo = await TodoApi.add(text, category);
        this.todos.push(todo);
      } catch (error) {
        this.setError(
          `Todo追加エラー: ${
            error instanceof Error ? error.message : String(error)
          }`
        );
      } finally {
        this.setLoading(false);
      }
    },

    /**
     * Todoの完了状態を切り替え
     */
    async toggleTodo(id: number) {
      this.clearError();

      try {
        const updated = await TodoApi.toggle(id);
        this.todos = this.todos.map((todo) =>
          todo.id === id ? updated : todo
        );
      } catch (error) {
        this.setError(
          `Todo更新エラー: ${
            error instanceof Error ? error.message : String(error)
          }`
        );
      }
    },

    /**
     * 指定IDのTodoを削除
     */
    async removeTodo(id: number) {
      this.clearError();

      try {
        await TodoApi.remove(id);
        this.todos = this.todos.filter((todo) => todo.id !== id);
      } catch (error) {
        this.setError(
          `Todo削除エラー: ${
            error instanceof Error ? error.message : String(error)
          }`
        );
      }
    },

    /**
     * すべてのTodoを削除
     */
    async removeAllTodos() {
      this.setLoading(true);
      this.clearError();

      try {
        await TodoApi.removeAll();
        this.todos = [];
      } catch (error) {
        this.setError(
          `全タスク削除エラー: ${
            error instanceof Error ? error.message : String(error)
          }`
        );
      } finally {
        this.setLoading(false);
      }
    },

    /**
     * カテゴリー別タスク削除
     * サーバーサイドでサポートされている場合はAPIを利用、そうでなければクライアント側で実装
     */
    async removeTodosByCategory(category: string) {
      if (!category || category === ALL_CATEGORIES) {
        this.setError("有効なカテゴリーを指定してください");
        return;
      }

      this.setLoading(true);
      this.clearError();

      try {
        // APIがサポートされていれば使用
        try {
          await TodoApi.removeByCategory(category);
          this.todos = this.todos.filter((todo) => todo.category !== category);
        } catch (apiError) {
          // API未サポートの場合はクライアント側で実装
          console.warn("カテゴリ削除API未サポート、クライアント側で処理します");
          this.todos = this.todos.filter((todo) => todo.category !== category);
        }
      } catch (error) {
        this.setError(
          `カテゴリー削除エラー: ${
            error instanceof Error ? error.message : String(error)
          }`
        );
      } finally {
        this.setLoading(false);
      }
    },
  },
  getters: {
    /**
     * 未完了タスク数
     */
    remaining: (state) => state.todos.filter((todo) => !todo.completed).length,

    /**
     * 特定カテゴリーの未完了タスク数
     */
    getRemainingByCategory: (state) => (category: string) => {
      if (category === ALL_CATEGORIES) {
        return state.todos.filter((todo) => !todo.completed).length;
      }
      return state.todos.filter(
        (todo) => todo.category === category && !todo.completed
      ).length;
    },

    /**
     * 特定カテゴリーのタスク
     */
    getTodosByCategory: (state) => (category: string) => {
      if (category === ALL_CATEGORIES) {
        return state.todos;
      }
      return state.todos.filter((todo) => todo.category === category);
    },

    /**
     * 現在読み込み中か
     */
    isLoading: (state) => state.loading,

    /**
     * 現在のエラーメッセージ
     */
    errorMessage: (state) => state.error,

    /**
     * カテゴリー別タスク数
     */
    todoCountByCategory: (state) => {
      const counts: Record<string, number> = {};
      state.todos.forEach((todo) => {
        const category = todo.category || "その他";
        counts[category] = (counts[category] || 0) + 1;
      });
      return counts;
    },
  },
});
