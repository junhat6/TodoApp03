<script setup lang="ts">
import { useTodoStore } from '../store/todo';
const store = useTodoStore()
</script>


<template>
<ul class="todo-list">
    <li v-for="todo in store.todos" :key="todo.id" :class="{completed: todo.completed}">
        <label>
            <input
            type="checkbox"
            :checked="todo.completed"
            @change="store.toggleTodo(todo.id)"
            />
            {{ todo.text }}
        </label>
        <button class="remove" @click="store.removeTodo(todo.id)">✗</button>
    </li>
</ul>
</template>

<style scoped>
.todo-list {
  list-style: none;
  padding: 0;
}
.todo-list li {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0.5rem;
  border-bottom: 1px solid #ddd;
}
.todo-list li.completed label {
  text-decoration: line-through;
  color: #999;
}
.todo-list button.remove {
  background: transparent;
  border: none;
  cursor: pointer;
  font-size: 1.2rem;
  line-height: 1;
}
</style>
