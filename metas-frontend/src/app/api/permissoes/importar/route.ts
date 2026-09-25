import { NextResponse } from "next/server";
import { getSession } from "@/lib/auth/session";
import { apiClient } from "@/lib/http/api-client";
import { ApiError } from "@/lib/http/api-error";
import { displayMessageOf } from "@/contracts/api/problem";

export async function POST(request: Request) {
  const session = await getSession();
  if (!session || !session.isAdmin) {
    return NextResponse.json({ message: "Acesso negado." }, { status: 403 });
  }

  const formData = await request.formData();

  try {
    const resultado = await apiClient("/permissoes/importar", { method: "POST", token: session.token, formData });
    return NextResponse.json(resultado);
  } catch (erro) {
    if (erro instanceof ApiError) {
      return NextResponse.json({ message: displayMessageOf(erro.problem) }, { status: erro.problem.status });
    }
    return NextResponse.json({ message: "Falha ao importar planilha de permissões." }, { status: 500 });
  }
}
