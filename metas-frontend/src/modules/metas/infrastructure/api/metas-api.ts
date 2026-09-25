import "server-only";
import { apiClient } from "@/lib/http/api-client";
import type { PreviewPlanilha } from "../../domain/entities/linha-meta";

export type UltimoUploadDto = {
  nomeArquivoOriginal: string;
  data: string;
  linhas: number;
  substituidas: number;
  novas: number;
};

export type ResumoUploadDto = { substituidas: number; novas: number };

export async function uploadPlanilha(token: string, formData: FormData): Promise<PreviewPlanilha> {
  return apiClient<PreviewPlanilha>("/metas/upload", { method: "POST", token, formData });
}

export async function confirmarUpload(token: string, payload: unknown): Promise<ResumoUploadDto> {
  return apiClient<ResumoUploadDto>("/metas/confirmar", { method: "POST", token, body: payload });
}

export async function obterUltimoUpload(token: string): Promise<UltimoUploadDto | null> {
  return apiClient<UltimoUploadDto | null>("/metas/ultimo-upload", { token });
}

export async function baixarModelo(token: string): Promise<ArrayBuffer> {
  return apiClient<ArrayBuffer>("/metas/modelo", { token });
}

export async function baixarUltimoUpload(token: string): Promise<ArrayBuffer> {
  return apiClient<ArrayBuffer>("/metas/ultimo-upload/download", { token });
}
