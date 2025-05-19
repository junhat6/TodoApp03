import { http, HttpResponse } from "msw";
import type { Todo } from "../store/todo";

let todos: Todo[] = [
  { id: 1, text: "サンプル１", completed: false, category: "仕事" },
  { id: 2, text: "サンプル２", completed: true, category: "個人" },
];

//  http.get<Params, RequestBodyType, ResponseBodyType, Path>(path, resolver)
/*  Params	object	Request path parameters. Narrows the params response resolver argument type.
    RequestBodyType	object	Request path parameters. Narrows the request.json() return type.
    ResponseBodyType	object	Request path parameters. Narrows the HttpResponse.text() and HttpResponse.json() response body type.
    Path	string	Request path. Narrows the path argument on the request handler.
*/
export const handlers = [
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
];
