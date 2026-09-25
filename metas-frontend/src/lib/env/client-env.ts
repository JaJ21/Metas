// Variáveis expostas ao navegador precisam do prefixo NEXT_PUBLIC_ e
// NUNCA podem conter segredo (URL interna da API, chaves, etc — essas
// ficam só em server-env.ts). Este projeto não precisa de nenhuma
// variável pública hoje; o arquivo existe como o "lugar certo" caso
// surja uma (ex: uma flag de feature visual).
export const clientEnv = {} as const;
