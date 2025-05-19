import { createApp } from "vue";
import { createPinia } from "pinia";
import App from "./App.vue";
import i18n from "./i18n";

// グローバルスタイルのインポート
import "./assets/styles/global.css";

// MSWのモックサーバーを開発環境で起動
if (import.meta.env.DEV && import.meta.env.VITE_USE_MSW === "true") {
  console.log("モックAPIを使用します");
  const { worker } = await import("./mocks/browser");
  await worker.start({
    onUnhandledRequest: "bypass", // 未処理のリクエストをスキップ
  });
}

const app = createApp(App);

// Piniaストアを追加
app.use(createPinia());
// i18nを追加
app.use(i18n);

app.mount("#app");
