import "server-only";
import { serverEnvSchema, type ServerEnv } from "./env-schema";

// "server-only" garante, em tempo de build, que este arquivo (e tudo
// que ele exporta) NUNCA acaba dentro do bundle enviado ao navegador —
// se algum Client Component tentar importar isso por engano, o build
// falha com um erro claro, em vez de vazar a URL interna da API.
let cache: ServerEnv | null = null;

export function serverEnv(): ServerEnv {
  if (cache) return cache;
  cache = serverEnvSchema.parse(process.env);
  return cache;
}
