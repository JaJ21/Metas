import { NextResponse } from "next/server";
import { loginCommand } from "@/modules/auth";
import { definirCookieDeSessao } from "@/lib/auth/session-cookie";
import { ApiError } from "@/lib/http/api-error";
import { displayMessageOf } from "@/contracts/api/problem";

/**
 * Único endpoint público de mutação do sistema. Recebe CPF/senha do
 * formulário (Client Component), chama o caso de uso, e — se dsignal
 * certo — grava o JWT recebido num cookie HttpOnly. O navegador nunca
 * vê o token em JavaScript, só o cookie (que ele nem consegue ler).
 */
export async function POST(request: Request) {
  const corpo: unknown = await request.json().catch(() => null);

  try {
    const resultado = await loginCommand(corpo);
    await definirCookieDeSessao(resultado.token);
    return NextResponse.json({ nome: resultado.nome, roles: resultado.roles });
  } catch (erro) {
    if (erro instanceof ApiError) {
      return NextResponse.json({ message: displayMessageOf(erro.problem) }, { status: erro.problem.status });
    }
    if (erro && typeof erro === "object" && "issues" in erro) {
      // erro de validação do Zod (loginInputSchema)
      return NextResponse.json({ message: "Dados de login inválidos." }, { status: 422 });
    }
    return NextResponse.json({ message: "Erro inesperado ao efetuar login." }, { status: 500 });
  }
}
