# Vue 3 + TypeScript + Vite

This template should help get you started developing with Vue 3 and TypeScript in Vite. The template uses Vue 3 `<script setup>` SFCs, check out the [script setup docs](https://v3.vuejs.org/api/sfc-script-setup.html#sfc-script-setup) to learn more.

Learn more about the recommended Project Setup and IDE Support in the [Vue Docs TypeScript Guide](https://vuejs.org/guide/typescript/overview.html#project-setup).

## 環境変数

`.env.local`ファイルで以下の環境変数を設定できます：

- `VITE_USE_MSW`: モックサービスワーカー（MSW）を有効にするかどうか
  - `true`: モックAPIを使用（デフォルト）
  - `false`: 実際のバックエンドAPIを使用
  
例：
```
VITE_USE_MSW=false
```
