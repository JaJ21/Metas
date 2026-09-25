"use client";

import { useEffect } from "react";
import { ErrorState } from "@/components/ui/error-state/error-state";
import { Button } from "@/components/ui/button/button";

export default function GlobalError({ error, reset }: { error: Error; reset: () => void }) {
  useEffect(() => {
    // Loga a causa real no console (a UI mostra só uma mensagem genérica
    // pro usuário). Sem isso, o erro que disparou o boundary se perde.
    console.error(error);
  }, [error]);

  return (
    <main className="flex min-h-screen flex-col items-center justify-center gap-4 p-6">
      <ErrorState mensagem="Algo deu errado. Tente novamente." />
      <Button onClick={reset}>Tentar de novo</Button>
    </main>
  );
}
