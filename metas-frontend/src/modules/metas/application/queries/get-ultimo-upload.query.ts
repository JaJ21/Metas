import { obterUltimoUpload, type UltimoUploadDto } from "../../infrastructure/api/metas-api";

export async function getUltimoUploadQuery(token: string): Promise<UltimoUploadDto | null> {
  return obterUltimoUpload(token);
}
