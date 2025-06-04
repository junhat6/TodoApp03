import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * Spring Boot with Kotlin プロジェクトのビルド設定
 * 
 * Gradle Kotlin DSL の特徴：
 * - 型安全なビルドスクリプト
 * - IDE支援（自動補完、リファクタリング）
 * - Kotlinの表現力を活用した設定記述
 */

plugins {
    // Spring Boot プラグイン
    // アプリケーションのパッケージングや実行機能を提供
    id("org.springframework.boot") version "3.2.4"
    
    // Spring Dependency Management プラグイン  
    // Spring Boot の依存関係バージョン管理を自動化
    id("io.spring.dependency-management") version "1.1.4"
    
    // Kotlin JVM プラグイン
    // Kotlin → JVMバイトコード コンパイルを有効化
    kotlin("jvm") version "1.9.22"
    
    // Kotlin Spring プラグイン
    // Spring向けKotlinの最適化（open クラス自動生成など）
    kotlin("plugin.spring") version "1.9.22"
    
    // Kotlin JPA プラグイン
    // JPA エンティティ向けの最適化（デフォルトコンストラクタ生成など）
    kotlin("plugin.jpa") version "1.9.22"
}

// プロジェクト基本情報
group = "com.example"
version = "0.0.1-SNAPSHOT"

/**
 * Java バージョン設定
 * 
 * Java 17 LTS の採用理由：
 * - Spring Boot 3.x の最低要件
 * - 長期サポート版（LTS）
 * - パフォーマンス向上とセキュリティ強化
 */
java {
    sourceCompatibility = JavaVersion.VERSION_17
}

/**
 * 依存関係リポジトリ設定
 * 
 * Maven Central：
 * - 最も標準的なJavaライブラリリポジトリ
 * - Spring Boot やサードパーティライブラリを取得
 */
repositories {
    mavenCentral()
}

/**
 * 依存関係定義
 * 
 * Spring Boot Starter の思想：
 * - 関連する依存関係をまとめてパッケージ化
 * - 設定の自動化によるゼロコンフィギュレーション
 * - オピニオネイテッドなデフォルト設定
 */
dependencies {
    // Spring Boot Web Starter
    // Web MVC、組み込みTomcat、JSON処理など Web アプリに必要な全て
    implementation("org.springframework.boot:spring-boot-starter-web")
    
    // Spring Boot JPA Starter  
    // JPA、Hibernate、トランザクション管理、データソース設定
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    
    // Spring Boot Validation Starter
    // Bean Validation（JSR-303）、Hibernate Validator
    implementation("org.springframework.boot:spring-boot-starter-validation")
    
    // Spring Boot Security Starter
    // 認証・認可、CSRF対策、セキュリティヘッダー設定
    implementation("org.springframework.boot:spring-boot-starter-security")
    
    // Jackson Kotlin Module
    // KotlinのデータクラスやNullableプロパティのJSON変換サポート
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    
    // Kotlin Reflection
    // Spring Framework がKotlinクラスを解析するために必要
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    
    // Flyway Core
    // データベースマイグレーション管理ツール
    // SQLスクリプトによるスキーマ変更の自動化
    implementation("org.flywaydb:flyway-core")
    
    // H2 Database (Runtime)
    // インメモリデータベース、開発・テスト環境で使用
    // runtimeOnly: コンパイル時は不要、実行時のみ必要
    runtimeOnly("com.h2database:h2")
    
    // PostgreSQL Driver (Runtime)  
    // 本番環境でのデータベース接続
    runtimeOnly("org.postgresql:postgresql")
    
    // Spring Boot Test Starter
    // テスト用ライブラリ一式（JUnit5、Mockito、TestContainers等）
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

/**
 * Kotlin コンパイル設定
 * 
 * JSR-305 サポート：
 * - Java ライブラリのnullability アノテーションを認識
 * - Spring Framework との相互運用性向上
 * 
 * JVM ターゲット：
 * - Java 17 バイトコードを生成
 * - ランタイム環境との整合性確保
 */
tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
        // JVMターゲットの不一致警告を抑制（一時的な対応）
        freeCompilerArgs += "-Xjvm-default=all"
    }
}

/**
 * テスト実行設定
 * 
 * JUnit Platform：
 * - JUnit 5 の実行エンジン
 * - 並列実行、条件付き実行などの先進機能
 */
tasks.withType<Test> {
    useJUnitPlatform()
} 