// プロフィール関連のAPI呼び出し
// 実際のAPIクライアントの代わりにfetchを使用
import type { Profile } from '../types/profile';

// APIレスポンスからプロフィールオブジェクトに変換
function parseProfileResponse(data: any): Profile {
  return {
    ...data,
    createdAt: new Date(data.createdAt),
    updatedAt: new Date(data.updatedAt),
  };
}

/**
 * プロフィール関連のAPI呼び出し
 */
export class ProfileApi {
  /**
   * プロフィール情報を取得
   */
  static async getProfile(): Promise<Profile> {
    const response = await fetch('/api/profile');
    if (!response.ok) {
      throw new Error(`プロフィール取得エラー: ${response.statusText}`);
    }
    const data = await response.json();
    return parseProfileResponse(data);
  }

  /**
   * プロフィール情報を更新
   */
  static async updateProfile(updates: Partial<Pick<Profile, 'username'>>): Promise<Profile> {
    const response = await fetch('/api/profile', {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(updates),
    });
    if (!response.ok) {
      const errorData = await response.json().catch(() => ({ error: response.statusText }));
      throw new Error(`プロフィール更新エラー: ${errorData.error || response.statusText}`);
    }
    const data = await response.json();
    return parseProfileResponse(data);
  }

  /**
   * レベルアップ
   */
  static async levelUp(): Promise<Profile> {
    const response = await fetch('/api/profile/level-up', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
    });
    if (!response.ok) {
      throw new Error(`レベルアップエラー: ${response.statusText}`);
    }
    const data = await response.json();
    return parseProfileResponse(data);
  }

  /**
   * プロフィール初期化（新規ユーザー用）
   */
  static async createProfile(username: string): Promise<Profile> {
    const response = await fetch('/api/profile', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ username }),
    });
    if (!response.ok) {
      const errorData = await response.json().catch(() => ({ error: response.statusText }));
      throw new Error(`プロフィール作成エラー: ${errorData.error || response.statusText}`);
    }
    const data = await response.json();
    return parseProfileResponse(data);
  }
} 