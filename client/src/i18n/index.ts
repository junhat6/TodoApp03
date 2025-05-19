import { createI18n } from "vue-i18n";
import ja from "./locales/ja.json";
import en from "./locales/en.json";

export default createI18n({
  legacy: false, // Vue 3向け設定
  locale: "ja", // デフォルト言語
  fallbackLocale: "en", // フォールバック言語
  messages: {
    ja,
    en,
  },
});
