export function LoadingState({ texto = "Carregando..." }: { texto?: string }) {
  return (
    <div role="status" className="flex items-center justify-center py-10 text-sm text-muted">
      {texto}
    </div>
  );
}
