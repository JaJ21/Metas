import { NextResponse } from "next/server";
import { getSession } from "@/lib/auth/session";
import { getUltimoUploadQuery } from "@/modules/metas";

export async function GET() {
  const session = await getSession();
  if (!session) {
    return NextResponse.json({ message: "Sessão expirada." }, { status: 401 });
  }

  const ultimo = await getUltimoUploadQuery(session.token);
  return NextResponse.json(ultimo);
}
