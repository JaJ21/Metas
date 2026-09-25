# Metas Backend (Java 25 / Spring Boot 4)

Backend do sistema de metas/orçamento, reescrito em Java seguindo o
padrão de engenharia da empresa (Clean/Hexagonal Architecture por
módulo, PostgreSQL + Flyway, Redis, Spring Security com JWT,
springdoc/OpenAPI).

## ⚠️ Aviso importante antes de rodar

Este projeto foi escrito num ambiente **sem acesso à internet** (sem
Maven Central, sem Gradle Plugin Portal) — por isso **não foi possível
compilar nem rodar `./gradlew build` durante o desenvolvimento**. O
código segue cuidadosamente a sintaxe e as APIs do Spring Boot 3.x/4.x,
mas antes de considerar pronto:

1. Rode `./gradlew build` localmente e corrija qualquer erro de
   compilação que aparecer (normal em um projeto desse tamanho escrito
   "às cegas").
2. Confirme as versões exatas de Spring Boot 4 / Java 25 disponíveis no
   seu repositório Maven (Nexus/Artifactory da empresa) — os números em
   `build.gradle.kts` seguem o padrão pedido, mas Spring Boot 4 é muito
   recente e pode exigir ajuste de patch version.
3. Rode os testes (`./gradlew test`) — o `ValidacaoPlanilhaPolicyTest` e
   o `RegrasDeArquiteturaTest` não dependem de banco, então são um bom
   primeiro sinal de que a base está saudável.

## Como rodar localmente

**Antes de tudo**: este projeto NÃO tem o Gradle Wrapper (`gradlew`) —
ele depende de um `.jar` que eu não conseguia baixar sem internet.
Resolva de um dos dois jeitos:

- **Terminal**: instale o Gradle localmente (`brew install gradle`,
  `choco install gradle`, ou via SDKMAN) e rode, uma única vez dentro
  desta pasta: `gradle wrapper --gradle-version 8.11`. Isso cria
  `gradlew`, `gradlew.bat` e `gradle/wrapper/` — dali em diante use
  `./gradlew` normalmente.
- **IntelliJ IDEA**: abra esta pasta como projeto Gradle — o IntelliJ
  baixa e gera o wrapper sozinho, e você roda `MetasApplication` direto
  pelo botão de play, sem precisar de terminal.

```bash
docker compose up -d          # sobe Postgres + Redis
cp .env.example .env          # ajuste os valores se precisar
./gradlew bootRun
```

A API sobe em `http://localhost:8080`. Documentação interativa (Swagger)
em `http://localhost:8080/docs`.

## Primeiro acesso

A migração `V6__seed_admin.sql` já cria um usuário administrador:

- **CPF**: `12345678900`
- **Senha**: `admin123`

Faça login em `POST /auth/login` com esse CPF/senha, pegue o token JWT
da resposta, e use no header `Authorization: Bearer <token>` nas demais
chamadas (ou cole o token no cadeado do Swagger UI). **Troque essa senha
assim que possível** — cadastre um novo admin de verdade e remova (ou
desative) esse usuário semente.

## O que foi portado do sistema em Python, e como

| Sistema em Python (Flask) | Backend Java |
|---|---|
| `config.py` (MESES_REAL/FORECAST/ORCADO) | `meta/core/domain/model/CicloOrcamentario.java` (objeto de domínio) + `application.yml` (`metas.*`) |
| `auth.py` (LDAP) | Login direto no backend com JWT (`auth` module) — ver nota abaixo |
| `excel_utils.py` (validação, upsert, planilha modelo) | `meta` module: `ValidacaoPlanilhaPolicy` (core) + `MetaGatewayImpl` (upsert) + `MetaPlanilhaService` (Apache POI) |
| `permissoes.py` | `permissao` module (Read/Write/Delete por CPF + Centro de Custo + Cod Conta, import/export) |
| `usuarios.py` | `usuario` module (CPF, Nome, Cargo, agora com senha) |
| `ultimos_uploads.py` | `meta/core/domain/gateway/UltimoUploadGateway` — guarda metadados + as chaves (Centro de Custo/Cod Conta) do último upload |
| Excel como "banco" | PostgreSQL (Flyway) — Qlik deve passar a ler direto do Postgres |

### Decisões importantes que mudaram em relação ao sistema em Python

1. **Autenticação**: o padrão pede "OAuth2 Resource Server com JWT de um
   provedor de identidade", mas você pediu login direto no backend. A
   solução: o próprio backend assina E valida o JWT (chave simétrica em
   `security.jwt.secret`). Quando a empresa tiver um IdP de verdade
   (Keycloak, Azure AD, etc), o único arquivo que precisa mudar é
   `shared/infra/config/security/JwtConfig.java` — troca o
   encoder/decoder simétrico por um decoder que busca a chave pública do
   IdP, sem tocar em mais nada do sistema.

2. **"Último upload" não guarda mais o arquivo exato**: como tudo virou
   banco relacional, `ultimo_upload` guarda só os METADADOS e QUAIS
   pares (Centro de Custo + Cod Conta) fizeram parte do upload. Baixar
   de novo reconstrói o `.xlsx` com os valores ATUAIS desses pares — se
   alguém mais alterou um desses pares depois, o download reflete isso
   (diferente do sistema em Python, que cacheava o arquivo exato). Avise
   os usuários dessa mudança de comportamento.

3. **Meses normalizados em tabela filha** (`meta_valor`), não uma coluna
   por mês — assim o schema não precisa de migração nova a cada virada
   de ciclo orçamentário. Só os valores em `application.yml`
   (`metas.ano-realizado`, `metas.ano-orcamento`, `metas.mes-corte-real`)
   precisam ser atualizados.

4. **Permissão "Delete"**: o campo existe na tabela `permissao` e na API,
   mas nenhuma ação de exclusão foi implementada ainda — igual estava
   combinado no sistema em Python, aguardando você detalhar o
   comportamento esperado.

## Principais endpoints

| Método | Rota | Requer | Descrição |
|---|---|---|---|
| POST | `/auth/login` | público | Login, devolve JWT |
| POST | `/metas/upload` | JWT | Sobe planilha, devolve preview |
| POST | `/metas/confirmar` | JWT | Confirma e salva (upsert) |
| GET | `/metas/modelo` | JWT | Baixa planilha modelo |
| GET | `/metas/ultimo-upload` | JWT | Metadados do último upload do usuário |
| GET | `/metas/ultimo-upload/download` | JWT | Baixa de novo (valores atuais) |
| POST/GET/DELETE | `/usuarios` | ADMIN | CRUD de usuários |
| POST/GET/DELETE | `/permissoes` | ADMIN | CRUD de permissões |
| POST | `/permissoes/importar` | ADMIN | Importa planilha (substitui tudo) |
| GET | `/permissoes/exportar` | ADMIN | Exporta planilha atual |

## Estrutura (por módulo, Clean/Hexagonal)

```
modulo/
├── core/
│   ├── domain/{model,gateway,enums,policy}   <- sem NENHUMA dependência de framework
│   ├── usecase/                               <- uma classe por ação de negócio
│   └── exception/
└── infra/
    ├── controller/{dto/request,dto/response,support}
    ├── persistence/{entity,repository,mapper,gateway}
    └── config/                                <- @Configuration que liga core à infra
```

`RegrasDeArquiteturaTest` (ArchUnit) fiscaliza automaticamente que o
`core` nunca importa Spring/JPA/Lombok — se isso acontecer, o build
falha.
