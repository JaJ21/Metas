import { NextResponse } from "next/server";
import { getSession } from "@/lib/auth/session";
import { confirmarUploadCommand } from "@/modules/metas";
import { ApiError } from "@/lib/http/api-error";
import { displayMessageOf } from "@/contracts/api/problem";

export async function POST(request: Request) {
  const session = await getSession();
  if (!session) {
    return NextResponse.json({ message: "Sessão expirada. Faça login novamente." }, { status: 401 });
  }

  const corpo: unknown = await request.json().catch(() => null);

  try {
    const resumo = await confirmarUploadCommand(session.token, corpo);
    return NextResponse.json(resumo);
  } catch (erro) {
    if (erro instanceof ApiError) {
      return NextResponse.json({ message: displayMessageOf(erro.problem) }, { status: erro.problem.status });
    }
    return NextResponse.json({ message: "Não foi possível confirmar o upload." }, { status: 422 });
  }
}
