import type { ReactNode } from "react";

/** Wrapper de tabela genérico, só com rolagem horizontal — sem conhecer nenhuma coluna específica de domínio. */
export function Table({ children }: { children: ReactNode }) {
  return (
    <div className="overflow-x-auto rounded-md border border-gray-200">
      <table className="w-full min-w-max border-collapse text-sm">{children}</table>
    </div>
  );
}
