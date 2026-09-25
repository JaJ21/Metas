import { NextResponse } from "next/server";
import { getSession } from "@/lib/auth/session";
import { apiClient } from "@/lib/http/api-client";

export async function GET() {
  const session = await getSession();
  if (!session || !session.isAdmin) {
    return NextResponse.json({ message: "Acesso negado." }, { status: 403 });
  }

  const arquivo = await apiClient<ArrayBuffer>("/permissoes/exportar", { token: session.token });
  return new NextResponse(arquivo, {
    headers: {
      "Content-Type": "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
      "Content-Disposition": "attachment; filename=permissoes.xlsx",
    },
  });
}
