import Link from "next/link";
import type { Session } from "@/lib/auth/session";
import { LogoutButton } from "./logout-button";

/** Server Component — decide se mostra os links de admin com base na sessão que já veio pronta do layout. */
export function Nav({ session }: { session: Session }) {
  return (
    <header className="flex items-center justify-between bg-primary px-6 py-3 text-white">
      <span className="font-semibold">Sistema de Metas</span>
      <nav className="flex items-center gap-4 text-sm">
        <span className="text-white/70">{session.nome}</span>
        <Link href="/metas/upload" className="hover:underline">Upload</Link>
        {session.isAdmin && (
          <>
            <Link href="/admin/usuarios" className="hover:underline">Usuários</Link>
            <Link href="/admin/permissoes" className="hover:underline">Permissões</Link>
          </>
        )}
        <LogoutButton />
      </nav>
    </header>
  );
}
