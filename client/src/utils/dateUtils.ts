/**
 * 日付フォーマットのユーティリティ関数
 */

/**
 * 日付を指定されたフォーマットで文字列に変換
 * @param date 日付オブジェクト
 * @param format フォーマット ('YYYY-MM-DD', 'MM/DD/YYYY', 'DD/MM/YYYY')
 * @returns フォーマットされた日付文字列
 */
export function formatDate(date: Date, format: string = 'YYYY-MM-DD'): string {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  
  switch (format) {
    case 'YYYY-MM-DD':
      return `${year}-${month}-${day}`;
    case 'MM/DD/YYYY':
      return `${month}/${day}/${year}`;
    case 'DD/MM/YYYY':
      return `${day}/${month}/${year}`;
    case 'YYYY/MM/DD':
      return `${year}/${month}/${day}`;
    default:
      return `${year}-${month}-${day}`;
  }
}

/**
 * 現在時刻の日付文字列を取得
 * @param format フォーマット
 * @returns 現在時刻の日付文字列
 */
export function getCurrentDateString(format: string = 'YYYY-MM-DD'): string {
  return formatDate(new Date(), format);
}

/**
 * 2つの日付の差を日数で取得
 * @param date1 比較する日付1
 * @param date2 比較する日付2
 * @returns 日数の差（date1 - date2）
 */
export function getDaysDifference(date1: Date, date2: Date): number {
  const oneDay = 24 * 60 * 60 * 1000; // 1日のミリ秒
  return Math.round((date1.getTime() - date2.getTime()) / oneDay);
}

/**
 * 指定した日数を加算した日付を取得
 * @param date 基準日
 * @param days 加算する日数（負の値で減算）
 * @returns 新しい日付オブジェクト
 */
export function addDays(date: Date, days: number): Date {
  const result = new Date(date);
  result.setDate(result.getDate() + days);
  return result;
}

/**
 * 日付が今日より前かどうかを判定
 * @param date 判定する日付
 * @returns 今日より前の場合true
 */
export function isPastDate(date: Date): boolean {
  const today = new Date();
  today.setHours(0, 0, 0, 0);
  const targetDate = new Date(date);
  targetDate.setHours(0, 0, 0, 0);
  return targetDate < today;
}

/**
 * 日付が今日かどうかを判定
 * @param date 判定する日付
 * @returns 今日の場合true
 */
export function isToday(date: Date): boolean {
  const today = new Date();
  return (
    date.getFullYear() === today.getFullYear() &&
    date.getMonth() === today.getMonth() &&
    date.getDate() === today.getDate()
  );
} 