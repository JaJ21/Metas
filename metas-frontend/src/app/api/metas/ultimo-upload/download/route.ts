import { NextResponse } from "next/server";
import { getSession } from "@/lib/auth/session";
import { baixarUltimoUpload } from "@/modules/metas/infrastructure/api/metas-api";

export async function GET() {
  const session = await getSession();
  if (!session) {
    return NextResponse.json({ message: "Sessão expirada." }, { status: 401 });
  }

  const arquivo = await baixarUltimoUpload(session.token);
  return new NextResponse(arquivo, {
    headers: {
      "Content-Type": "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
      "Content-Disposition": `attachment; filename=ultimo_upload_${session.cpf}.xlsx`,
    },
  });
}
