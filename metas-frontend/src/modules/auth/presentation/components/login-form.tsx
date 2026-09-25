"use client";

import { useState, type FormEvent } from "react";
import { useRouter } from "next/navigation";
import { Button } from "@/components/ui/button/button";
import { Input } from "@/components/ui/input/input";
import { ErrorState } from "@/components/ui/error-state/error-state";

/**
 * Client Component só porque precisa de estado local e evento de
 * submit (regra 6.2 do documento). A chamada de verdade vai pra
 * /api/auth/login (Route Handler), NUNCA direto pro backend Java — o
 * navegador não tem (e não pode ter) a URL nem credenciais da API de
 * negócio.
 */
export function LoginForm() {
  const router = useRouter();
  const [cpf, setCpf] = useState("");
  const [senha, setSenha] = useState("");
  const [erro, setErro] = useState<string | null>(null);
  const [enviando, setEnviando] = useState(false);

  async function handleSubmit(evento: FormEvent) {
    evento.preventDefault();
    setErro(null);
    setEnviando(true);

    try {
      const resposta = await fetch("/api/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ cpf, senha }),
      });

      if (!resposta.ok) {
        const corpo = await resposta.json().catch(() => ({ message: "Não foi possível entrar." }));
        setErro(corpo.message ?? "Não foi possível entrar.");
        return;
      }

      router.push("/metas/upload");
      router.refresh();
    } finally {
      setEnviando(false);
    }
  }

  return (
    <form onSubmit={handleSubmit} className="flex w-full max-w-sm flex-col gap-4">
      <Input
        label="CPF"
        name="cpf"
        inputMode="numeric"
        placeholder="Somente números"
        value={cpf}
        onChange={(e) => setCpf(e.target.value.replace(/\D/g, ""))}
        maxLength={11}
        required
        autoFocus
      />
      <Input
        label="Senha"
        name="senha"
        type="password"
        value={senha}
        onChange={(e) => setSenha(e.target.value)}
        required
      />
      {erro && <ErrorState mensagem={erro} />}
      <Button type="submit" carregando={enviando}>
        Entrar
      </Button>
    </form>
  );
}
