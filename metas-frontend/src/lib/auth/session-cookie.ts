import "server-only";
import { cookies } from "next/headers";
import { serverEnv } from "@/lib/env/server-env";

/**
 * Onde o JWT emitido pelo backend Java fica guardado: um cookie
 * HttpOnly (o JavaScript do navegador NÃO consegue ler), Secure em
 * produção, e SameSite=Lax. Isso é literalmente a seção 7.1 do
 * documento de arquitetura — "tokens de acesso não devem ser
 * persistidos em localStorage".
 */
export async function definirCookieDeSessao(token: string) {
  const { SESSION_COOKIE_NAME, SESSION_COOKIE_SECURE } = serverEnv();
  const store = await cookies();
  store.set(SESSION_COOKIE_NAME, token, {
    httpOnly: true,
    secure: SESSION_COOKIE_SECURE,
    sameSite: "lax",
    path: "/",
    // Mesmo prazo do token JWT no backend (security.jwt.expiration-minutes,
    // 480 min = 8h por padrão) — não faz sentido o cookie durar mais que o token.
    maxAge: 60 * 60 * 8,
  });
}

export async function obterTokenDaSessao(): Promise<string | null> {
  const { SESSION_COOKIE_NAME } = serverEnv();
  const store = await cookies();
  return store.get(SESSION_COOKIE_NAME)?.value ?? null;
}

export async function removerCookieDeSessao() {
  const { SESSION_COOKIE_NAME } = serverEnv();
  const store = await cookies();
  store.delete(SESSION_COOKIE_NAME);
}
