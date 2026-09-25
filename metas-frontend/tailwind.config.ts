import type { Config } from "tailwindcss";

export default {
  content: ["./src/**/*.{ts,tsx}"],
  theme: {
    extend: {
      colors: {
        // Tokens de cor do design system — ver src/styles/tokens.css.
        // Mantidos aqui como referência a variáveis CSS, não valores
        // fixos, pra permitir tema claro/escuro no futuro sem tocar
        // em componente nenhum.
        primary: "var(--color-primary)",
        "primary-foreground": "var(--color-primary-foreground)",
        danger: "var(--color-danger)",
        success: "var(--color-success)",
        muted: "var(--color-muted)",
      },
    },
  },
  plugins: [],
} satisfies Config;
