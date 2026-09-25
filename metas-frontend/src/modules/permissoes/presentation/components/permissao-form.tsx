import { Button } from "@/components/ui/button/button";
import { Input } from "@/components/ui/input/input";
import { salvarPermissaoAction } from "../../permissoes.actions";

export function PermissaoForm() {
  return (
    <form action={salvarPermissaoAction} className="mb-4 flex flex-wrap items-end gap-2 rounded-md border border-gray-200 p-4">
      <Input label="CPF" name="cpf" required />
      <Input label="Centro de Custo" name="centroCusto" required />
      <Input label="Cod Conta" name="codConta" required />
      <label className="flex items-center gap-1 pb-2 text-sm"><input type="checkbox" name="read" /> Read</label>
      <label className="flex items-center gap-1 pb-2 text-sm"><input type="checkbox" name="write" /> Write</label>
      <label className="flex items-center gap-1 pb-2 text-sm"><input type="checkbox" name="delete" /> Delete</label>
      <Button type="submit">Salvar</Button>
    </form>
  );
}
