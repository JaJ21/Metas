import { FlatCompat } from "@eslint/eslintrc";

const compat = new FlatCompat({ baseDirectory: import.meta.dirname });

export default [
  ...compat.extends("next/core-web-vitals", "next/typescript"),
  {
    rules: {
      // Regra prática que espelha a seção 4 do documento de arquitetura:
      // evita import relativo cruzando módulos por engano (ex: metas
      // importando direto de dentro de permissoes/infrastructure).
      "no-restricted-imports": [
        "warn",
        { patterns: ["../../*/infrastructure/*", "../../*/domain/*"] },
      ],
    },
  },
];
