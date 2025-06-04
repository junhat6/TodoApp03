/**
 * Todoアプリケーションで使用するカテゴリー定義
 * 型安全性を保ちながら、カテゴリーの一元管理を行う
 */

// カテゴリー識別子（変更不可の値）
// as const を使用してリテラル型として扱い、型安全性を向上
export const CATEGORY_IDS = {
  ALL: "all",           // 全カテゴリー表示用（フィルタリング用）
  WORK: "work",         // 仕事関連のタスク
  PERSONAL: "personal", // 個人的なタスク
  SHOPPING: "shopping", // 買い物リスト
  OTHER: "other",       // その他のタスク
} as const;

// カテゴリーリスト（i18nのキーとして使用）
// 配列として定義し、UIでのループ処理に使用
export const CATEGORIES = [
  CATEGORY_IDS.ALL,
  CATEGORY_IDS.WORK,
  CATEGORY_IDS.PERSONAL,
  CATEGORY_IDS.SHOPPING,
  CATEGORY_IDS.OTHER,
] as const;

// カテゴリー型（型安全に使用するため）
// ユニオン型として定義し、指定された値のみ受け入れる
export type CategoryId = (typeof CATEGORIES)[number];

// デフォルトカテゴリー
// 新規タスク作成時の初期値
export const DEFAULT_CATEGORY = CATEGORY_IDS.PERSONAL;

// 全カテゴリー表示用の特別値
// フィルタリング機能で「すべて」を選択した時に使用
export const ALL_CATEGORIES = CATEGORY_IDS.ALL;
