# Metas Frontend (Next.js)

Front-end do sistema de metas, seguindo a arquitetura descrita em
`ARCHITECTURE-NEXTJS.md` (monólito modular: domain → application →
infrastructure → presentation, por módulo), adaptada ao domínio real
deste projeto (não aos módulos de CMS do documento original).

## ⚠️ Aviso importante antes de rodar

Este projeto foi escrito **sem acesso à internet** (sem npm registry
completo para os pacotes do Next.js/React) — por isso **não foi
possível rodar `npm install` nem `npm run build`/`npm run dev` durante o
desenvolvimento**. Antes de considerar pronto:

1. `npm install` e resolva quaisquer incompatibilidades de versão entre
   Next.js 15 / React 19 (usados aqui) e o que estiver disponível no seu
   ambiente.
2. `npm run typecheck` — o TypeScript vai pegar qualquer erro de tipo
   que eu não tenha conseguido validar sem compilar.
3. `npm run build` e `npm run dev` pra ver a aplicação rodando de
   verdade.
4. `npm test` — os poucos testes de unidade que escrevi (funções puras)
   não dependem de navegador nem de rede, então são um bom primeiro
   sinal de saúde do projeto.

## Como rodar

```bash
cp .env.example .env.local     # aponte METAS_API_URL pro backend Java rodando
npm install
npm run dev
```

Certifique-se de que o **backend Java** (`metas-backend/`) já está
rodando em `http://localhost:8080` (ou o endereço configurado em
`METAS_API_URL`) — este front-end não funciona sozinho, ele é só a
camada de composição/proteção na frente da API de negócio.

## Como a arquitetura foi aplicada

| Regra do documento | Como foi aplicada aqui |
|---|---|
| Navegador nunca fala com a API de negócio | Todo `fetch` pro backend Java está em `lib/http/api-client.ts`, marcado `server-only`. O navegador só chama rotas internas (`/api/...`) ou usa Server Actions. |
| Sessão em cookie HttpOnly | `lib/auth/session-cookie.ts` — o JWT do backend nunca chega a rodar em `localStorage` nem em JS do navegador. |
| Server Components por padrão | Todas as `page.tsx` são `async function` sem `"use client"`; só os pedaços realmente interativos (upload+preview editável, formulário de login) são Client Components. |
| Server Actions para mutação simples | Cadastro/remoção de usuário e permissão (`*.actions.ts`) — formulário HTML puro, sem JavaScript de submit. |
| Route Handlers para fluxos mais ricos | Upload de planilha, confirmação, download de arquivo, import/export de permissões — passam por `app/api/...` porque envolvem arquivo binário ou precisam do retorno estruturado pro Client Component de preview. |
| Erro RFC 7807 | `contracts/api/problem.ts` — o mesmo formato que o `GlobalExceptionHandler` do backend Java devolve. |
| `core`/`domain` sem framework | `modules/*/domain/*` não importa Next.js nem React em nenhum arquivo. |

## O que ficou simplificado (documentar débito técnico)

- **Sem refresh token**: o backend ainda não emite um. `refreshSessionIfNeeded()` existe como no-op documentado em `lib/auth/session.ts`, pronto pra ganhar lógica quando o backend tiver.
- **Sem paginação nas listas de usuários/permissões**: o backend hoje devolve tudo de uma vez (`GET /usuarios`, `GET /permissoes`); o contrato `PageResult<T>` já existe em `contracts/pagination/`, é só o backend passar a paginar quando o volume justificar.
- **Módulos do documento original não implementados**: o `ARCHITECTURE-NEXTJS.md` descreve um CMS genérico (conteúdo, audiências, questionários, etc) — só a ESTRUTURA/regras foram aproveitadas; os módulos de negócio aqui são só `auth`, `metas`, `permissoes` e `usuarios`, que é o que este sistema realmente tem.
- **Testes end-to-end (Playwright)**: não escritos — o documento pede cobertura E2E dos fluxos críticos (login, upload, confirmação), mas isso exige a aplicação rodando de verdade, o que não foi possível validar neste ambiente.

## Estrutura

```
src/
├── app/                 <- rotas (App Router) — só composição, nada de regra de negócio
│   ├── (public)/login/
│   ├── (private)/       <- protegido por requireSession() no layout.tsx
│   └── api/              <- Route Handlers (proxy autenticado pro backend)
├── modules/
│   ├── auth/
│   ├── metas/            <- upload, preview editável, confirmação, último upload
│   ├── permissoes/
│   └── usuarios/
│       ├── domain/        <- sem framework
│       ├── application/   <- commands/queries/dto
│       ├── infrastructure/<- chamadas HTTP ao backend
│       └── presentation/  <- componentes + view-models
├── components/ui/        <- genéricos, sem regra de domínio
├── contracts/             <- ApiProblem (RFC 7807), PageResult
├── lib/
│   ├── http/              <- cliente HTTP único (server-only)
│   ├── auth/               <- sessão via cookie HttpOnly
│   └── env/                <- variáveis de ambiente validadas (zod)
└── test/
```
