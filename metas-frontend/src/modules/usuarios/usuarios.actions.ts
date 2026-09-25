"use server";

import { revalidatePath } from "next/cache";
import { requireSession, requirePermission } from "@/lib/auth/session";
import { salvarUsuarioCommand, removerUsuarioCommand } from "./index";

/**
 * Server Actions — seção 6.4 do documento: usadas aqui porque é uma
 * mutação simples de formulário interno (cadastro de usuário), sem
 * necessidade de expor uma API pública pra isso. A autorização é
 * checada DENTRO da action (requirePermission), nunca só escondendo o
 * botão na tela.
 */
export async function salvarUsuarioAction(formData: FormData): Promise<void> {
  const session = await requireSession();
  await requirePermission(session, "ADMIN");

  await salvarUsuarioCommand(session.token, {
    cpf: formData.get("cpf"),
    nome: formData.get("nome"),
    cargo: formData.get("cargo"),
    senha: formData.get("senha"),
    admin: formData.get("admin") === "on",
  });

  revalidatePath("/admin/usuarios");
}

export async function removerUsuarioAction(formData: FormData): Promise<void> {
  const session = await requireSession();
  await requirePermission(session, "ADMIN");

  const cpf = String(formData.get("cpf"));
  await removerUsuarioCommand(session.token, cpf);

  revalidatePath("/admin/usuarios");
}
