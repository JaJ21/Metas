import { requireSession, requirePermission } from "@/lib/auth/session";
import { listarUsuariosQuery } from "@/modules/usuarios";
import { UsuarioForm } from "@/modules/usuarios/presentation/components/usuario-form";
import { UsuariosTable } from "@/modules/usuarios/presentation/components/usuarios-table";

export default async function UsuariosPage() {
  const session = await requireSession();
  await requirePermission(session, "ADMIN");

  const usuarios = await listarUsuariosQuery(session.token);

  return (
    <div>
      <h1 className="mb-4 text-lg font-semibold">Usuários</h1>
      <UsuarioForm />
      <UsuariosTable usuarios={usuarios} />
    </div>
  );
}
