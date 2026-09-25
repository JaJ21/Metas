import "server-only";

/**
 * Decodifica (sem verificar assinatura) os claims do JWT — só pra
 * exibir informação na tela (nome, se é admin) e decidir se esconde um
 * link de menu. A VERIFICAÇÃO de verdade do token acontece no backend
 * Java a cada chamada de API (ver SecurityConfig lá); se alguém
 * adulterar o cookie, a chamada à API simplesmente falha com 401 — essa
 * função aqui nunca é a fonte de autorização real, só de exibição.
 */
export type JwtClaims = {
  sub: string; // CPF
  nome: string;
  roles: string[];
  exp: number;
};

export function decodificarClaims(token: string): JwtClaims | null {
  try {
    const [, payloadBase64] = token.split(".");
    if (!payloadBase64) return null;
    const json = Buffer.from(payloadBase64, "base64url").toString("utf-8");
    const claims = JSON.parse(json) as JwtClaims;
    if (claims.exp * 1000 < Date.now()) return null; // token expirado
    return claims;
  } catch {
    return null;
  }
}
