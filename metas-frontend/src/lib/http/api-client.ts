import "server-only";
import { serverEnv } from "@/lib/env/server-env";
import { ApiError } from "./api-error";
import { isApiProblem, type ApiProblem } from "@/contracts/api/problem";
import { novoIdDeCorrelacao } from "./request-context";

/**
 * Cliente HTTP ÚNICO pra falar com o backend de negócio (o Java Spring).
 * Regra do documento de arquitetura (seção 6.5): o navegador NUNCA fala
 * direto com a API de negócio — só o servidor (Server Components, Route
 * Handlers, Server Actions) chama este arquivo, e é por isso que ele
 * importa "server-only": se um Client Component tentar importar isso,
 * o build quebra na hora, em vez de vazar a URL/token pro navegador.
 *
 * Responsabilidades (e só essas — nada de regra de negócio aqui):
 *   - montar a URL a partir da variável de ambiente
 *   - anexar o token (quando informado)
 *   - header de correlação
 *   - timeout
 *   - converter erro HTTP em ApiError (com o ApiProblem já tipado)
 */

type Opcoes = {
  method?: "GET" | "POST" | "PUT" | "PATCH" | "DELETE";
  token?: string | null;
  body?: unknown;
  /** Para envio de arquivos (multipart/form-data) — quando presente, ignora "body". */
  formData?: FormData;
  timeoutMs?: number;
  /** Política de cache do Next.js — "no-store" é o padrão para dados administrativos privados (seção 6.7). */
  cache?: RequestCache;
};

export async function apiClient<T>(caminho: string, opcoes: Opcoes = {}): Promise<T> {
  const { METAS_API_URL } = serverEnv();
  const controller = new AbortController();
  const timeout = setTimeout(() => controller.abort(), opcoes.timeoutMs ?? 15_000);

  const headers: Record<string, string> = {
    "X-Correlation-Id": novoIdDeCorrelacao(),
  };
  if (opcoes.token) {
    headers.Authorization = `Bearer ${opcoes.token}`;
  }
  if (opcoes.body !== undefined) {
    headers["Content-Type"] = "application/json";
  }

  try {
    const resposta = await fetch(`${METAS_API_URL}${caminho}`, {
      method: opcoes.method ?? "GET",
      headers,
      body: opcoes.formData ?? (opcoes.body !== undefined ? JSON.stringify(opcoes.body) : undefined),
      signal: controller.signal,
      cache: opcoes.cache ?? "no-store",
    });

    if (!resposta.ok) {
      throw new ApiError(await extrairProblem(resposta));
    }

    if (resposta.status === 204) {
      return undefined as T;
    }

    const contentType = resposta.headers.get("content-type") ?? "";
    if (contentType.includes("application/json")) {
      return (await resposta.json()) as T;
    }
    // Downloads de arquivo (planilha .xlsx) — devolve o ArrayBuffer bruto.
    return (await resposta.arrayBuffer()) as T;
  } catch (erro) {
    if (erro instanceof ApiError) throw erro;
    if (erro instanceof Error && erro.name === "AbortError") {
      throw new ApiError({ title: "Tempo esgotado", status: 504, displayMessage: "O servidor demorou demais para responder. Tente novamente." });
    }
    throw new ApiError({ title: "Falha de conexão", status: 503, displayMessage: "Não foi possível falar com o servidor. Tente novamente em instantes." });
  } finally {
    clearTimeout(timeout);
  }
}

async function extrairProblem(resposta: Response): Promise<ApiProblem> {
  try {
    const corpo: unknown = await resposta.json();
    if (isApiProblem(corpo)) return corpo;
  } catch {
    // resposta sem corpo JSON — segue pro fallback abaixo
  }
  return { title: resposta.statusText || "Erro", status: resposta.status };
}
