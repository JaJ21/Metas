import Link from "next/link";

export default function NotFound() {
  return (
    <main className="flex min-h-screen flex-col items-center justify-center gap-2 p-6 text-center">
      <h1 className="text-lg font-semibold">Página não encontrada</h1>
      <Link href="/" className="text-sm text-primary underline">Voltar ao início</Link>
    </main>
  );
}
