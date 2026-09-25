import { NextResponse } from "next/server";
import { getSession } from "@/lib/auth/session";
import { uploadPlanilhaCommand } from "@/modules/metas";
import { ApiError } from "@/lib/http/api-error";
import { displayMessageOf } from "@/contracts/api/problem";

/**
 * Recebe o arquivo do navegador (multipart), anexa o token da sessão
 * (que o navegador nunca vê) e repassa pro backend Java. É essa
 * "ilha" server-side que cumpre a regra 6.5: o navegador nunca fala
 * direto com a API de negócio.
 */
export async function POST(request: Request) {
  const session = await getSession();
  if (!session) {
    return NextResponse.json({ message: "Sessão expirada. Faça login novamente." }, { status: 401 });
  }

  const formData = await request.formData();

  try {
    const preview = await uploadPlanilhaCommand(session.token, formData);
    return NextResponse.json(preview);
  } catch (erro) {
    if (erro instanceof ApiError) {
      return NextResponse.json({ message: displayMessageOf(erro.problem) }, { status: erro.problem.status });
    }
    return NextResponse.json({ message: "Erro inesperado ao processar a planilha." }, { status: 500 });
  }
}
