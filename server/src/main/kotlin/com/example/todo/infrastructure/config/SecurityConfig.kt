package com.example.todo.infrastructure.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

/**
 * Spring Security の設定クラス
 * 
 * Spring Security の役割：
 * - 認証（Authentication）：ユーザーが誰であるかを確認
 * - 認可（Authorization）：認証されたユーザーの権限確認
 * - セキュリティ脅威からの保護（CSRF、XSS等）
 * 
 * @Configuration：Spring設定クラスであることを示す
 * @EnableWebSecurity：Spring Security機能を有効化
 * - セキュリティフィルターチェーンの自動設定
 * - デフォルトのセキュリティ設定を適用
 */
@Configuration
@EnableWebSecurity
class SecurityConfig {
    
    /**
     * セキュリティフィルターチェーンの設定
     * 
     * フィルターチェーンパターン：
     * - リクエストが複数のセキュリティフィルターを順次通過
 
     * - 認証、認可、CORS、CSRFなどの処理を段階的に実行
     * 
     * 開発環境向けの緩い設定：
     * - 認証なしでAPIアクセス可能
     * - 本番環境では適切な認証機構の実装が必要
     */
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            // CSRF（Cross-Site Request Forgery）保護を無効化
            // SPA（Single Page Application）では通常無効にする
            // - ステートレスなAPIサーバーのため
            // - JWTトークンベース認証で代替
            .csrf { it.disable() }
            
            // CORS設定を適用
            // フロントエンドからのクロスオリジンリクエストを許可
            .cors { it.configurationSource(corsConfigurationSource()) }
            
            // 認可設定：全てのリクエストを許可
            // 実際のプロダクションでは適切な権限制御が必要
            .authorizeHttpRequests { auth ->
                auth.anyRequest().permitAll()  // 全エンドポイントへのアクセスを許可
            }
        return http.build()
    }

    /**
     * CORS設定の詳細定義
     * 
     * プログラマティックCORS設定：
     * - WebConfigのCORSと重複するが、より詳細な制御が可能
     * - Spring Security レベルでのCORS処理
     * 
     * 設定項目の詳細説明：
     * - allowedOrigins: 許可するオリジン（フロントエンドURL）
     * - allowedMethods: 許可するHTTPメソッド
     * - allowedHeaders: 許可するリクエストヘッダー（認証ヘッダー含む）
     * - exposedHeaders: レスポンスでクライアントに公開するヘッダー
     * - allowCredentials: Cookie、認証情報の送信許可
     * - maxAge: プリフライトリクエストのキャッシュ時間
     */
    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        
        // 開発環境のフロントエンドURL（Vite）
        configuration.allowedOrigins = listOf("http://localhost:5173")
        
        // RESTful APIで使用する標準的なHTTPメソッド
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
        
        // 認証やコンテンツタイプなど、一般的なリクエストヘッダー
        configuration.allowedHeaders = listOf(
            "Authorization",           // JWT等の認証トークン
            "Content-Type",           // JSON等のコンテンツタイプ  
            "X-Requested-With",       // Ajax リクエストの識別
            "Accept",                 // 受け入れ可能なレスポンス形式
            "Origin",                 // リクエスト元のオリジン
            "Access-Control-Request-Method",    // プリフライトで要求されるメソッド
            "Access-Control-Request-Headers"    // プリフライトで要求されるヘッダー
        )
        
        // クライアントが読み取り可能なレスポンスヘッダー
        configuration.exposedHeaders = listOf(
            "Access-Control-Allow-Origin",      // CORS許可オリジン
            "Access-Control-Allow-Credentials"  // 認証情報許可
        )
        
        // 認証情報（Cookie、Authorization ヘッダー等）の送信を許可
        configuration.allowCredentials = true
        
        // プリフライトリクエストの結果を1時間キャッシュ
        configuration.maxAge = 3600L

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)  // 全パスに適用
        return source
    }
} 