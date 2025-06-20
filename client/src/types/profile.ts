/**
 * プロフィール関連の型定義
 */

export interface Profile {
  id: string;
  username: string;
  level: number;
  createdAt: Date;
  updatedAt: Date;
}

export interface ProfileState {
  profile: Profile | null;
  loading: boolean;
  error: string | null;
} 