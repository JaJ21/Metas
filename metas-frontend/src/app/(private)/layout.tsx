import { requireSession } from "@/lib/auth/session";
import { Nav } from "@/components/layout/nav";

/**
 * Nível 1 de autorização (seção 7.2 do documento): "middleware ou
 * layout para proteger áreas inteiras". Todo mundo dentro de
 * app/(private)/... passa por aqui primeiro — se não tiver sessão,
 * requireSession() já redireciona pro /login antes de renderizar
 * qualquer coisa da área protegida.
 */
export default async function PrivateLayout({ children }: { children: React.ReactNode }) {
  const session = await requireSession();

  return (
    <div className="min-h-screen bg-gray-50">
      <Nav session={session} />
      <main className="mx-auto max-w-6xl p-6">{children}</main>
    </div>
  );
}
