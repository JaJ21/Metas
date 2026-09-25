import "server-only";
import { redirect } from "next/navigation";
import { obterTokenDaSessao, removerCookieDeSessao } from "./session-cookie";
import { decodificarClaims } from "./jwt-claims";

export type Session = {
  token: string;
  cpf: string;
  nome: string;
  roles: string[];
  isAdmin: boolean;
};

/**
 * As cinco funções que o documento de arquitetura pede na seção 7.1.
 * "refreshSessionIfNeeded" é um no-op aqui de propósito: o backend
 * ainda não emite refresh token (ver README do backend) — quando ele
 * passar a emitir, esta é a única função que precisa ganhar lógica de
 * verdade, sem mudar nada nas telas que já chamam requireSession().
 */
export async function getSession(): Promise<Session | null> {
  const token = await obterTokenDaSessao();
  if (!token) return null;

  const claims = decodificarClaims(token);
  if (!claims) return null;

  return {
    token,
    cpf: claims.sub,
    nome: claims.nome,
    roles: claims.roles ?? [],
    isAdmin: (claims.roles ?? []).includes("ADMIN"),
  };
}

export async function requireSession(): Promise<Session> {
  const session = await getSession();
  if (!session) {
    redirect("/login");
  }
  return session;
}

export async function requirePermission(session: Session, role: string): Promise<void> {
  if (!session.roles.includes(role)) {
    redirect("/");
  }
}

export async function refreshSessionIfNeeded(): Promise<void> {
  // Sem refresh token no backend ainda — ver comentário acima.
}

export async function revokeSession(): Promise<void> {
  await removerCookieDeSessao();
}
