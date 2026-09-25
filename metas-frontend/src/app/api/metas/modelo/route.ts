import { NextResponse } from "next/server";
import { getSession } from "@/lib/auth/session";
import { baixarModelo } from "@/modules/metas/infrastructure/api/metas-api";

export async function GET() {
  const session = await getSession();
  if (!session) {
    return NextResponse.json({ message: "Sessão expirada." }, { status: 401 });
  }

  const arquivo = await baixarModelo(session.token);
  return new NextResponse(arquivo, {
    headers: {
      "Content-Type": "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
      "Content-Disposition": "attachment; filename=modelo_planilha.xlsx",
    },
  });
}
