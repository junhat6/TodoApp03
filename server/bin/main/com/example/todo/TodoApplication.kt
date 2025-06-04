package com.example.todo

// Spring Bootの自動設定アノテーションをインポート
import org.springframework.boot.autoconfigure.SpringBootApplication
// Spring Bootアプリケーションを起動する関数をインポート
import org.springframework.boot.runApplication

/**
 * Todo アプリケーションのメインクラス
 * 
 * @SpringBootApplication アノテーションの役割：
 * - @Configuration: このクラスがBean定義のソースであることを示す
 * - @EnableAutoConfiguration: Spring Bootの自動設定を有効にする
 * - @ComponentScan: このパッケージ以下でComponent、Service、Repository等をスキャンする
 * 
 * Spring Bootでは、このアノテーション一つで Web、JPA、セキュリティなどの
 * 必要な設定が自動的に行われる（Convention over Configuration の思想）
 */
@SpringBootApplication
class TodoApplication

/**
 * アプリケーションのエントリーポイント
 * 
 * Kotlinの fun main は Java の public static void main に相当
 * args: Array<String> で コマンドライン引数を受け取る
 * 
 * runApplication<TodoApplication>(*args) の説明：
 * - <TodoApplication>: Kotlinのジェネリクス、起動するアプリケーションクラスを指定
 * - *args: Kotlinのスプレッド演算子、配列を可変長引数として展開
 */
fun main(args: Array<String>) {
    // Spring Bootアプリケーションを起動
    // 内部では組み込みTomcatサーバーが起動し、HTTPリクエストを受け付ける準備をする
    runApplication<TodoApplication>(*args)
} 