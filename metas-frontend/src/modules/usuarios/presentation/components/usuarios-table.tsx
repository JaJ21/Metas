import { Table } from "@/components/ui/table/table";
import { EmptyState } from "@/components/ui/empty-state/empty-state";
import type { Usuario } from "../../domain/entities/usuario";
import { removerUsuarioAction } from "../../usuarios.actions";

export function UsuariosTable({ usuarios }: { usuarios: Usuario[] }) {
  if (usuarios.length === 0) {
    return <EmptyState titulo="Nenhum usuário cadastrado ainda." />;
  }

  return (
    <Table>
      <thead>
        <tr className="bg-gray-50 text-left">
          <th className="p-2">CPF</th>
          <th className="p-2">Nome</th>
          <th className="p-2">Cargo</th>
          <th className="p-2">Admin</th>
          <th className="p-2"></th>
        </tr>
      </thead>
      <tbody>
        {usuarios.map((usuario) => (
          <tr key={usuario.cpf} className="border-t border-gray-100">
            <td className="p-2">{usuario.cpf}</td>
            <td className="p-2">{usuario.nome}</td>
            <td className="p-2">{usuario.cargo ?? "—"}</td>
            <td className="p-2">{usuario.admin ? "Sim" : "—"}</td>
            <td className="p-2">
              <form action={removerUsuarioAction}>
                <input type="hidden" name="cpf" value={usuario.cpf} />
                <button type="submit" className="text-xs text-danger underline">Remover</button>
              </form>
            </td>
          </tr>
        ))}
      </tbody>
    </Table>
  );
}
