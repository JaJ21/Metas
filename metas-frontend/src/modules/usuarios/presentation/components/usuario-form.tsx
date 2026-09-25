import { Button } from "@/components/ui/button/button";
import { Input } from "@/components/ui/input/input";
import { salvarUsuarioAction } from "../../usuarios.actions";

/**
 * Formulário simples que envia direto pra uma Server Action (atributo
 * "action" no <form>, sem handler de JavaScript) — não precisa de
 * 'use client' nem de estado local, o próprio Next.js cuida do envio.
 */
export function UsuarioForm() {
  return (
    <form action={salvarUsuarioAction} className="mb-6 flex flex-wrap items-end gap-2 rounded-md border border-gray-200 p-4">
      <Input label="CPF" name="cpf" required maxLength={11} inputMode="numeric" />
      <Input label="Nome" name="nome" required />
      <Input label="Cargo" name="cargo" />
      <Input label="Senha (opcional se já existe)" name="senha" type="password" />
      <label className="flex items-center gap-1 pb-2 text-sm">
        <input type="checkbox" name="admin" /> Admin
      </label>
      <Button type="submit">Salvar</Button>
    </form>
  );
}
