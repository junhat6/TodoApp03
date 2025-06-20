import { http, HttpResponse } from "msw";
import type { Todo } from "../types/todo";
import type { Profile } from "../types/profile";

let todos: Todo[] = [
  { id: 1, text: "サンプル１", completed: false, category: "仕事" },
  { id: 2, text: "サンプル２", completed: true, category: "個人" },
];

// プロフィールのモックデータ
let profile: Profile = {
  id: '1',
  username: 'ユーザー太郎',
  level: 5,
  createdAt: new Date('2024-01-01'),
  updatedAt: new Date(),
};

// プロフィールレスポンス用の変換関数
function toProfileResponse(profile: Profile) {
  return {
    ...profile,
    createdAt: profile.createdAt.toISOString(),
    updatedAt: profile.updatedAt.toISOString(),
  };
}

// エラーレスポンス用の型
interface ErrorResponse {
  error: string;
}

//  http.get<Params, RequestBodyType, ResponseBodyType, Path>(path, resolver)
/*  Params	object	Request path parameters. Narrows the params response resolver argument type.
    RequestBodyType	object	Request path parameters. Narrows the request.json() return type.
    ResponseBodyType	object	Request path parameters. Narrows the HttpResponse.text() and HttpResponse.json() response body type.
    Path	string	Request path. Narrows the path argument on the request handler.
*/
export const handlers = [
  // Todo関連のハンドラー
  http.get<never, undefined, Todo[], "/api/todos">("/api/todos", () => {
    return HttpResponse.json(todos);
  }),
  http.post<never, { text: string; category: string }, Todo, "/api/todos">(
    "/api/todos",
    async ({ request }) => {
      const data = await request.json();
      const newId = Math.max(0, ...todos.map((t) => t.id)) + 1;
      const newTodo = {
        id: newId,
        text: data.text,
        completed: false,
        category: data.category || "個人",
      };
      todos.push(newTodo);
      return HttpResponse.json(newTodo);
    }
  ),
  http.delete<{ id: string }, undefined, undefined, "/api/todos/:id">(
    "/api/todos/:id",
    ({ params }) => {
      todos = todos.filter((todo) => todo.id !== Number(params.id));
      return HttpResponse.json(null);
    }
  ),
  http.put<{ id: string }, { completed: boolean }, undefined, "/api/todos/:id">(
    "/api/todos/:id",
    ({ params }) => {
      const todo = todos.find((t) => t.id === Number(params.id));
      if (!todo)
        return HttpResponse.json({ error: "Todo not found" }, { status: 404 });
      todo.completed = !todo.completed;
      return HttpResponse.json(todo);
    }
  ),
  http.delete<never, undefined, undefined, "/api/todos">("/api/todos", () => {
    todos = [];
    return HttpResponse.json({ message: "All todos deleted" });
  }),

  // プロフィール関連のハンドラー
  http.get("/api/profile", () => {
    // リアルなAPIレスポンスタイムをシミュレート
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve(HttpResponse.json(toProfileResponse(profile)));
      }, 300);
    });
  }),

  http.put("/api/profile", async ({ request }) => {
    const updates = await request.json() as any;
    
    // バリデーション
    if (updates?.username && updates.username.trim().length < 1) {
      return HttpResponse.json(
        { error: "ユーザー名は1文字以上である必要があります" },
        { status: 400 }
      );
    }

    // プロフィール更新
    profile = {
      ...profile,
      ...(updates || {}),
      username: updates?.username?.trim() || profile.username,
      updatedAt: new Date(),
    };

    return new Promise((resolve) => {
      setTimeout(() => {
        resolve(HttpResponse.json(toProfileResponse(profile)));
      }, 500);
    });
  }),

  http.post("/api/profile/level-up", () => {
    // レベルアップロジック
    profile = {
      ...profile,
      level: profile.level + 1,
      updatedAt: new Date(),
    };

    return new Promise((resolve) => {
      setTimeout(() => {
        resolve(HttpResponse.json(toProfileResponse(profile)));
      }, 200);
    });
  }),

  http.post("/api/profile", async ({ request }) => {
    const data = await request.json() as any;
    
    // バリデーション
    if (!data?.username || data.username.trim().length < 1) {
      return HttpResponse.json(
        { error: "ユーザー名は必須です" },
        { status: 400 }
      );
    }

    // 新規プロフィール作成
    profile = {
      id: '1',
      username: data.username.trim(),
      level: 1,
      createdAt: new Date(),
      updatedAt: new Date(),
    };

    return new Promise((resolve) => {
      setTimeout(() => {
        resolve(HttpResponse.json(toProfileResponse(profile)));
      }, 800);
    });
  }),
];
