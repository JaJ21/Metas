import { requireSession, requirePermission } from "@/lib/auth/session";
import { listarPermissoesQuery } from "@/modules/permissoes";
import { PermissaoForm } from "@/modules/permissoes/presentation/components/permissao-form";
import { PermissoesTable } from "@/modules/permissoes/presentation/components/permissoes-table";

export default async function PermissoesPage() {
  const session = await requireSession();
  await requirePermission(session, "ADMIN");

  const permissoes = await listarPermissoesQuery(session.token);

  return (
    <div>
      <h1 className="mb-4 text-lg font-semibold">Permissões</h1>
      <PermissaoForm />

      <form
        action="/api/permissoes/importar"
        method="post"
        encType="multipart/form-data"
        className="mb-4 flex items-center gap-2"
      >
        <input type="file" name="arquivo" accept=".xlsx,.xls" required className="text-sm" />
        <button type="submit" className="text-sm text-primary underline">Importar (substitui tudo)</button>
      </form>
      <a href="/api/permissoes/exportar" className="mb-4 inline-block text-sm text-primary underline">
        Exportar planilha atual
      </a>

      <PermissoesTable permissoes={permissoes} />
    </div>
  );
}
