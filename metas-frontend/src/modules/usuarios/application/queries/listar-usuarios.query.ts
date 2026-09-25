import { listarUsuarios } from "../../infrastructure/api/usuarios-api";

export async function listarUsuariosQuery(token: string) {
  return listarUsuarios(token);
}
