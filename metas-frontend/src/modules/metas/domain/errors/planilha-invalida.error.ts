export class PlanilhaInvalidaError extends Error {
  constructor(message: string) {
    super(message);
    this.name = "PlanilhaInvalidaError";
  }
}
