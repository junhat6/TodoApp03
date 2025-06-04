package com.example.todo.infrastructure.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * Webアプリケーションの設定クラス
 * 
 * Spring MVC 設定のカスタマイズ：
 * - WebMvcConfigurer インターフェースの実装
 * - 必要なメソッドのみオーバーライド（デフォルト実装利用）
 * - アプリケーション固有の Web 設定を定義
 * 
 * @Configuration アノテーション：
 * - Springコンテナに設定クラスとして認識させる
 * - @Bean メソッドの定義が可能
 * - コンポーネントスキャンの対象
 */
@Configuration
class WebConfig : WebMvcConfigurer {

    /**
     * CORS（Cross-Origin Resource Sharing）の設定
     * 
     * CORS が必要な理由：
     * - フロントエンド（localhost:5173）とバックエンド（localhost:8080）のオリジンが異なる
     * - ブラウザの同一オリジンポリシーによりリクエストがブロックされる
     * - 明示的にクロスオリジンアクセスを許可する必要がある
     * 
     * 設定項目の説明：
     * - allowedOrigins: 許可するオリジン（フロントエンドのURL）
     * - allowedMethods: 許可するHTTPメソッド
     * - allowedHeaders: 許可するリクエストヘッダー
     * - allowCredentials: 認証情報（Cookie等）の送信を許可
     * - maxAge: プリフライトリクエストのキャッシュ時間（秒）
     * 
     * セキュリティ考慮：
     * - 本番環境では allowedOrigins を適切に制限
     * - "*" の使用は避ける（セキュリティリスク）
     */
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/api/**")                          // API エンドポイントに対してCORS設定を適用
            .allowedOrigins("http://localhost:5173")            // Viteのデフォルトポート（開発環境）
            .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")  // RESTful APIの標準メソッド
            .allowedHeaders("*")                                // 全てのリクエストヘッダーを許可
            .allowCredentials(true)                             // 認証情報の送信を許可
            .maxAge(3600)                                       // 1時間のキャッシュ
    }
} 