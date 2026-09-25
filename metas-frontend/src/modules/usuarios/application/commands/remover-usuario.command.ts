import { removerUsuario } from "../../infrastructure/api/usuarios-api";

export async function removerUsuarioCommand(token: string, cpf: string) {
  await removerUsuario(token, cpf);
}
