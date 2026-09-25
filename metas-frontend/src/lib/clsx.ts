/** Utilitário mínimo de concatenação condicional de classes CSS, sem trazer uma dependência externa só pra isso. */
export function clsx(...classes: Array<string | false | null | undefined>): string {
  return classes.filter(Boolean).join(" ");
}
