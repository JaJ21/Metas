import { requireSession } from "@/lib/auth/session";
import { getUltimoUploadQuery } from "@/modules/metas";
import { UltimoUploadCard } from "@/modules/metas/presentation/components/ultimo-upload-card";
import { UploadWidget } from "@/modules/metas/presentation/components/upload-widget";

/**
 * Server Component (padrão da seção 6.1 do documento): a consulta do
 * "último upload" é feita AQUI, no servidor, antes de qualquer HTML
 * chegar ao navegador — nada de fazer essa mesma consulta de novo
 * dentro de um Client Component.
 */
export default async function UploadPage() {
  const session = await requireSession();
  const ultimo = await getUltimoUploadQuery(session.token);

  return (
    <div>
      <h1 className="mb-1 text-lg font-semibold">Upload da planilha de metas</h1>
      <p className="mb-4 text-sm text-muted">
        Envie a planilha padrão. Nenhuma célula de dado pode ter letra, exceto a Justificativa.
      </p>

      <UltimoUploadCard ultimo={ultimo} />

      <a href="/api/metas/modelo" className="mb-4 inline-block text-sm text-primary underline">
        Baixar modelo padrão da planilha
      </a>

      <UploadWidget />
    </div>
  );
}
