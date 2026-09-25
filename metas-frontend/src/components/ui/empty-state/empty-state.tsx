export function EmptyState({ titulo, descricao }: { titulo: string; descricao?: string }) {
  return (
    <div className="flex flex-col items-center justify-center gap-1 py-10 text-center">
      <p className="text-sm font-medium text-gray-700">{titulo}</p>
      {descricao && <p className="text-xs text-muted">{descricao}</p>}
    </div>
  );
}
