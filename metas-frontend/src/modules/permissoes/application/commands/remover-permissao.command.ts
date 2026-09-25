import { removerPermissao } from "../../infrastructure/api/permissoes-api";

export async function removerPermissaoCommand(token: string, cpf: string, centroCusto: string, codConta: string) {
  await removerPermissao(token, cpf, centroCusto, codConta);
}
