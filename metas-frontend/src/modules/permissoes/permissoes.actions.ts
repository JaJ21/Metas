"use server";

import { revalidatePath } from "next/cache";
import { requireSession, requirePermission } from "@/lib/auth/session";
import { salvarPermissaoCommand, removerPermissaoCommand } from "./index";

export async function salvarPermissaoAction(formData: FormData): Promise<void> {
  const session = await requireSession();
  await requirePermission(session, "ADMIN");

  await salvarPermissaoCommand(session.token, {
    cpf: formData.get("cpf"),
    centroCusto: formData.get("centroCusto"),
    codConta: formData.get("codConta"),
    read: formData.get("read") === "on",
    write: formData.get("write") === "on",
    delete: formData.get("delete") === "on",
  });

  revalidatePath("/admin/permissoes");
}

export async function removerPermissaoAction(formData: FormData): Promise<void> {
  const session = await requireSession();
  await requirePermission(session, "ADMIN");

  await removerPermissaoCommand(
    session.token,
    String(formData.get("cpf")),
    String(formData.get("centroCusto")),
    String(formData.get("codConta"))
  );

  revalidatePath("/admin/permissoes");
}
