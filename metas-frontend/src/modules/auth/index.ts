// Fachada pública do módulo — outros módulos/telas importam DAQUI, nunca
// de dentro de application/infrastructure/domain diretamente (regra 4.6
// do documento de arquitetura).
export { loginCommand, loginInputSchema } from "./application/commands/login.command";
export type { LoginInput, LoginResult } from "./application/commands/login.command";
