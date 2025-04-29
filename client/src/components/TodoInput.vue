<script setup lang="ts">
import { ref } from 'vue';
import { useTodoStore } from '../store/todo';

const store = useTodoStore()
const text = ref('')

function onAdd() {
    const v = text.value &&text.value.trim()
    if(!v) return

    store.addTodo(v)
    text.value = ''

}
</script>

<template>
    <div class="todo-input">
        <input 
        v-model="text"
        @keyup.enter="onAdd"
        placeholder="新しいタスクを入力"
        />
        <button @click="onAdd">追加</button>
    </div>
</template>


<style scoped>
.todo-input {
  display: flex;
  gap: 0.5rem;
}
.todo-input input {
  flex: 1;
  padding: 0.5rem;
}
.todo-input button {
  padding: 0.5rem 1rem;
}
</style>