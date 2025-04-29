<script setup lang="ts">
import TodoInput from './components/TodoInput.vue';
import TodoList from './components/TodoList.vue';
import { useTodoStore } from './store/todo';

const store = useTodoStore()
</script>

<template>
  <h1>My Todo App</h1>
  <TodoInput />
  <TodoList />
  <p>残り： {{ store.remaining }}</p>
</template>

<style>
body {
  font-family: sans-serif;
  padding: 2rem;
}
</style>
