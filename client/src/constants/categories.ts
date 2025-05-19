/**
 * Todoアプリケーションで使用するカテゴリー定義
 */

// カテゴリー識別子（変更不可の値）
export const CATEGORY_IDS = {
  ALL: "all",
  WORK: "work",
  PERSONAL: "personal",
  SHOPPING: "shopping",
  OTHER: "other",
} as const;

// カテゴリーリスト（i18nのキーとして使用）
export const CATEGORIES = [
  CATEGORY_IDS.ALL,
  CATEGORY_IDS.WORK,
  CATEGORY_IDS.PERSONAL,
  CATEGORY_IDS.SHOPPING,
  CATEGORY_IDS.OTHER,
] as const;

// カテゴリー型（型安全に使用するため）
export type CategoryId = (typeof CATEGORIES)[number];

// デフォルトカテゴリー
export const DEFAULT_CATEGORY = CATEGORY_IDS.PERSONAL;

// 全カテゴリー表示用の特別値
export const ALL_CATEGORIES = CATEGORY_IDS.ALL;
