import { defineStore } from "pinia";
import { TodoApi } from "../api/todo";
import type { Todo } from "../types/todo";
import { ALL_CATEGORIES } from "../constants/categories";
import { profileStore } from "./profile";

interface TodoState {
  todos: Todo[];
  nextId: number;
  loading: boolean;
  error: string | null;
}

/**
 * Todoの状態管理ストア
 * アプリケーション全体でTodoに関する状態とロジックを管理
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

      // 3秒後にエラーをクリア（UXの向上）
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
     * アプリケーション起動時やリフレッシュ時に呼び出される
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
     * @param text - タスクの内容
     * @param category - タスクのカテゴリー
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
     * @param id - 切り替えるTodoのID
     */
    async toggleTodo(id: number) {
      this.clearError();

      try {
        const todoToToggle = this.todos.find(todo => todo.id === id);
        const updated = await TodoApi.toggle(id);
        this.todos = this.todos.map((todo) =>
          todo.id === id ? updated : todo
        );
        
        // タスクが完了状態になった場合、レベルアップを実行
        if (todoToToggle && !todoToToggle.completed && updated.completed) {
          await profileStore.levelUp();
        }
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
     * @param id - 削除するTodoのID
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
     * 一括削除機能で使用
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
     * @param category - 削除するカテゴリー
     */
    async removeTodosByCategory(category: string) {
      if (!category || category === ALL_CATEGORIES) {
        this.setError("有効なカテゴリーを指定してください");
        return;
      }

      this.setLoading(true);
      this.clearError();

      try {
        try {
          await TodoApi.removeByCategory(category);
          this.todos = this.todos.filter((todo) => todo.category !== category);
        } catch (apiError) {
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
     * ホーム画面での統計表示に使用
     */
    remaining: (state) => state.todos.filter((todo) => !todo.completed).length,

    /**
     * 特定カテゴリーの未完了タスク数
     * カテゴリー別の統計表示に使用
     * @param category - 対象カテゴリー
     * @returns カテゴリー内の未完了タスク数
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
     * カテゴリー別表示のフィルタリングに使用
     * @param category - 対象カテゴリー
     * @returns カテゴリーに属するタスクの配列
     */
    getTodosByCategory: (state) => (category: string) => {
      if (category === ALL_CATEGORIES) {
        return state.todos;
      }
      return state.todos.filter((todo) => todo.category === category);
    },

    /**
     * 現在読み込み中か
     * ローディングスピナーの表示制御に使用
     */
    isLoading: (state) => state.loading,

    /**
     * 現在のエラーメッセージ
     * エラー表示の制御に使用
     */
    errorMessage: (state) => state.error,

    /**
     * カテゴリー別タスク数
     * 統計情報やダッシュボード表示に使用可能
     * @returns カテゴリー名をキー、タスク数を値とするオブジェクト
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
