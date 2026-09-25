import { type ButtonHTMLAttributes, forwardRef } from "react";
import { clsx } from "@/lib/clsx";

type Variante = "primaria" | "secundaria" | "perigo";

type Props = ButtonHTMLAttributes<HTMLButtonElement> & {
  variante?: Variante;
  carregando?: boolean;
};

const estilosPorVariante: Record<Variante, string> = {
  primaria: "bg-primary text-primary-foreground hover:opacity-90",
  secundaria: "bg-white border border-gray-300 hover:bg-gray-50",
  perigo: "bg-danger text-white hover:opacity-90",
};

/**
 * Componente genérico — não conhece nenhuma entidade de negócio (regra
 * 3.3 do documento: components/ui não pode ter regra de domínio). Só
 * cuida de variante visual, estado de carregamento e acessibilidade
 * básica (foco visível, desabilitado enquanto carrega).
 */
export const Button = forwardRef<HTMLButtonElement, Props>(function Button(
  { variante = "primaria", carregando, disabled, className, children, ...rest },
  ref
) {
  return (
    <button
      ref={ref}
      disabled={disabled || carregando}
      className={clsx(
        "inline-flex items-center justify-center rounded-md px-4 py-2 text-sm font-medium transition-colors",
        "focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-primary",
        "disabled:opacity-50 disabled:cursor-not-allowed",
        estilosPorVariante[variante],
        className
      )}
      {...rest}
    >
      {carregando ? "Aguarde..." : children}
    </button>
  );
});
