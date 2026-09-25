export function ErrorState({ mensagem }: { mensagem: string }) {
  return (
    <div role="alert" className="rounded-md border border-danger bg-red-50 px-4 py-3 text-sm text-danger">
      {mensagem}
    </div>
  );
}
