import type { NextConfig } from "next";

// Limite de upload maior que o padrão do App Router, para caber a
// planilha de metas (o backend também tem seu próprio limite, ver
// config.MAX_CONTENT_LENGTH do sistema original — 10 MB).
const nextConfig: NextConfig = {
  experimental: {
    serverActions: {
      bodySizeLimit: "10mb",
    },
  },
};

export default nextConfig;
