import { Table } from "@/components/ui/table/table";
import { EmptyState } from "@/components/ui/empty-state/empty-state";
import type { Permissao } from "../../domain/entities/permissao";
import { removerPermissaoAction } from "../../permissoes.actions";

export function PermissoesTable({ permissoes }: { permissoes: Permissao[] }) {
  if (permissoes.length === 0) {
    return <EmptyState titulo="Nenhuma permissão cadastrada ainda." />;
  }

  return (
    <Table>
      <thead>
        <tr className="bg-gray-50 text-left">
          <th className="p-2">CPF</th>
          <th className="p-2">Centro de Custo</th>
          <th className="p-2">Cod Conta</th>
          <th className="p-2">Read</th>
          <th className="p-2">Write</th>
          <th className="p-2">Delete</th>
          <th className="p-2"></th>
        </tr>
      </thead>
      <tbody>
        {permissoes.map((p, i) => (
          <tr key={i} className="border-t border-gray-100">
            <td className="p-2">{p.cpf}</td>
            <td className="p-2">{p.centroCusto}</td>
            <td className="p-2">{p.codConta}</td>
            <td className="p-2">{p.read ? "Sim" : "—"}</td>
            <td className="p-2">{p.write ? "Sim" : "—"}</td>
            <td className="p-2">{p.delete ? "Sim" : "—"}</td>
            <td className="p-2">
              <form action={removerPermissaoAction}>
                <input type="hidden" name="cpf" value={p.cpf} />
                <input type="hidden" name="centroCusto" value={p.centroCusto} />
                <input type="hidden" name="codConta" value={p.codConta} />
                <button type="submit" className="text-xs text-danger underline">Remover</button>
              </form>
            </td>
          </tr>
        ))}
      </tbody>
    </Table>
  );
}
