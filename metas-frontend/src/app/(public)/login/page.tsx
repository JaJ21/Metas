import { LoginForm } from "@/modules/auth/presentation/components/login-form";

export default function LoginPage() {
  return (
    <main className="flex min-h-screen items-center justify-center bg-gray-50 px-4">
      <div className="flex w-full max-w-sm flex-col gap-6 rounded-lg border border-gray-200 bg-white p-8 shadow-sm">
        <div>
          <h1 className="text-lg font-semibold">Sistema de Metas</h1>
          <p className="text-sm text-muted">Entre com seu CPF e senha.</p>
        </div>
        <LoginForm />
      </div>
    </main>
  );
}
