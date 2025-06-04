// Vue 3のアプリケーション作成関数をインポート
// Vue 3では createApp が新しいアプリケーションインスタンスを作成する
import { createApp } from "vue";

// Pinia（Vue 3の公式状態管理ライブラリ）のストア作成関数をインポート
// Vuex の後継として推奨される状態管理ソリューション
import { createPinia } from "pinia";

// ルートコンポーネント（メイン画面）をインポート
import App from "./App.vue";

// i18n（国際化）設定をインポート
// 多言語サポートのためのライブラリ
import i18n from "./i18n";

// グローバルスタイルのインポート
// 全体的なデザインシステムとテーマ設定
import "./assets/styles/global.css";

// MSW（Mock Service Worker）のモックサーバーを開発環境で起動
// 実際のバックエンドAPIが無い場合でも、フロントエンド開発を進められる
if (import.meta.env.DEV && import.meta.env.VITE_USE_MSW === "true") {
  console.log("モックAPIを使用します");
  // 動的インポートを使用してMSWのワーカーを遅延読み込み
  // 本番環境では実行されないため、バンドルサイズを最適化
  const { worker } = await import("./mocks/browser");
  
  // MSWワーカーを開始
  await worker.start({
    onUnhandledRequest: "bypass", // 未処理のリクエストをスキップ
  });
}

// Vue 3アプリケーションインスタンスを作成
// App.vue をルートコンポーネントとして設定
const app = createApp(App);

// Piniaストアを追加
// アプリケーション全体で状態管理が可能になる
app.use(createPinia());

// i18nを追加
// アプリケーション全体で多言語化が可能になる
app.use(i18n);

// アプリケーションをDOMの #app 要素にマウント
// index.html の <div id="app"></div> に Vue アプリが描画される
app.mount("#app");
