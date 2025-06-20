import { ref, computed } from 'vue';
import type { Profile, ProfileState } from '../types/profile';
import { ProfileApi } from '../api/profile';

/**
 * プロフィール管理ストア
 */
class ProfileStore {
  private state = ref<ProfileState>({
    profile: null,
    loading: false,
    error: null,
  });

  // ゲッター
  get profile() {
    return computed(() => this.state.value.profile);
  }

  get loading() {
    return computed(() => this.state.value.loading);
  }

  get error() {
    return computed(() => this.state.value.error);
  }

  // 初期化（APIからプロフィールデータを取得）
  async initializeProfile() {
    this.state.value.loading = true;
    this.state.value.error = null;

    try {
      const profile = await ProfileApi.getProfile();
      this.state.value.profile = profile;
    } catch (error) {
      this.state.value.error = error instanceof Error ? error.message : 'プロフィール取得エラー';
      console.error('プロフィール初期化エラー:', error);
    } finally {
      this.state.value.loading = false;
    }
  }

  // プロフィール更新
  async updateProfile(updates: Partial<Pick<Profile, 'username'>>) {
    if (!this.state.value.profile) return;

    this.state.value.loading = true;
    this.state.value.error = null;

    try {
      const updatedProfile = await ProfileApi.updateProfile(updates);
      this.state.value.profile = updatedProfile;
    } catch (error) {
      this.state.value.error = error instanceof Error ? error.message : 'プロフィール更新エラー';
      console.error('プロフィール更新エラー:', error);
    } finally {
      this.state.value.loading = false;
    }
  }

  // レベルアップ（タスク完了時などに呼び出し）
  async levelUp() {
    if (!this.state.value.profile) return;

    try {
      const updatedProfile = await ProfileApi.levelUp();
      this.state.value.profile = updatedProfile;
    } catch (error) {
      this.state.value.error = error instanceof Error ? error.message : 'レベルアップエラー';
      console.error('レベルアップエラー:', error);
    }
  }

  // 新規プロフィール作成
  async createProfile(username: string) {
    this.state.value.loading = true;
    this.state.value.error = null;

    try {
      const profile = await ProfileApi.createProfile(username);
      this.state.value.profile = profile;
    } catch (error) {
      this.state.value.error = error instanceof Error ? error.message : 'プロフィール作成エラー';
      console.error('プロフィール作成エラー:', error);
    } finally {
      this.state.value.loading = false;
    }
  }

  // エラークリア
  clearError() {
    this.state.value.error = null;
  }
}

// シングルトンインスタンス
export const profileStore = new ProfileStore(); 