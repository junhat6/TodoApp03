import { defineStore } from "pinia";

export interface Todo {
  id: number;
  text: string;
  completed: boolean;
}

export const useTodoStore = defineStore("todo", {
  state: () => ({
    todos: [] as Todo[],
    nextId: 1 as number,
  }),
  actions: {
    addTodo(text: string) {
      this.todos.push({ id: this.nextId, text, completed: false });
      this.nextId++;
    },
    toggleTodo(id: number) {
      const todo = this.todos.find((c) => c.id === id);
      if (todo) {
        todo.completed = !todo.completed;
      }
    },
    removeTodo(id: number) {
      this.todos = this.todos.filter((c) => c.id !== id);
    },
  },
  getters: {
    remaining: (state) => state.todos.filter((c) => !c.completed).length,
  },
});
