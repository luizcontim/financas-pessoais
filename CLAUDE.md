# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
docker compose up -d          # sobe o Postgres (necessário para rodar a app e para os testes *IT)
./mvnw spring-boot:run         # roda a aplicação em localhost:8080
./mvnw verify                  # roda testes de domínio (JUnit puro) e de integração (Testcontainers)
./mvnw test -Dtest=CartaoDeCreditoTest        # roda uma única classe de teste
./mvnw test -Dtest=CartaoDeCreditoTest#nome_do_metodo   # roda um único método de teste
```

Testes `*Test.java` (em `dominio/modelo`) são unitários puros, sem contexto Spring. Testes `*IT.java` sobem um Postgres real via Testcontainers e rodam na fase `verify` (maven-failsafe-plugin), não em `test` — precisam do Docker ativo, mas não do `docker compose up -d` manual (o Testcontainers gerencia o próprio container).

CI (`.github/workflows/ci.yml`) roda `./mvnw -B verify` em push/PR para `main`.

## Agent skills

### Issue tracker

Issues vivem no GitHub Issues do repositório `luizcontim/financas-pessoais` (usa a CLI `gh`). Veja `docs/agents/issue-tracker.md`.

### Domain docs

Layout single-context: `CONTEXT.md` e `docs/adr/` na raiz do repositório (ainda não criados; serão gerados sob demanda pela skill de domain-modeling). Veja `docs/agents/domain.md`.

### Spec-Driven Development

Toda feature nova nasce de uma spec em `docs/specs/` (a partir de `docs/specs/TEMPLATE.md`) *antes* da implementação — descreve contexto, requisitos, casos de uso, o que fica de fora e decisões técnicas. `docs/specs/0001-nucleo-transacional.md` documenta retroativamente o que já está implementado. A skill `spec-feature` automatiza esse fluxo; use-a quando o pedido for uma feature nova (não um bugfix pontual).

### Revisão DDD

Depois de implementar ou alterar código de domínio/aplicação, use a skill `revisao-ddd` — ela checa aderência às regras arquiteturais deste projeto específico (ver checklist detalhado em `.claude/skills/revisao-ddd/SKILL.md`, resumido na seção Arquitetura abaixo).

## Arquitetura

Spring Boot 4 / Java 25, arquitetura hexagonal (ports & adapters) com as regras de negócio isoladas em `dominio`, sem nenhuma dependência de framework:

```
com.luizcontim.financas
├── dominio/            # entidades, agregados, value objects e ports — zero Spring/JPA aqui
│   ├── modelo/           # CartaoDeCredito, Conta, Fatura, Dinheiro, Compra, Parcela, Movimentacao...
│   ├── repositorio/      # interfaces de porta (CartaoRepositorio, ContaRepositorio)
│   └── excecao/          # exceções de domínio
├── aplicacao/
│   ├── casodeuso/        # um caso de uso por operação (CriarCartaoUseCase, RegistrarCompraUseCase...) — Java puro, sem anotações Spring
│   └── dto/              # DTOs de entrada/saída dos casos de uso
├── infraestrutura/
│   ├── persistencia/     # adapters JPA: *RepositorioJpa implementam as portas de dominio.repositorio, delegando a *SpringDataRepository; entidade/ tem os *JpaEntity; mapeador/ converte entidade JPA ↔ modelo de domínio
│   └── web/              # adapters REST: controlador/ (Controllers + GlobalExceptionHandler), dto/ (Request/Response da API)
└── config/               # fiação dos casos de uso como beans do Spring (CasoDeUsoConfig) — único lugar que liga tudo
```

Regras de dependência a preservar ao editar qualquer camada:

- **`dominio/`** nunca importa `org.springframework.*`, `jakarta.persistence.*` nem nada de `infraestrutura/`. Violação aqui é o achado mais grave em revisão.
- **`aplicacao/casodeuso/`** não tem anotações do Spring (`@Service`, etc.) e não depende de `infraestrutura/`; um caso de uso só orquestra (busca → chama método do agregado → salva), nunca decide regra de negócio — isso vive no agregado.
- **`infraestrutura/persistencia`** nunca expõe `*JpaEntity` como se fosse o modelo de domínio; a conversão sempre passa pelos mappers dedicados (`ContaMapper`, `CartaoCreditoMapper`).
- DTOs (`aplicacao/dto`, `infraestrutura/web/dto`) usam tipos primitivos/`BigDecimal`/`String`/enums, nunca tipos agregados de domínio atravessando a fronteira web.
- **`Dinheiro`** é o value object para todo valor monetário (nunca `BigDecimal`/`double` soltos em domínio/aplicação) — centraliza arredondamento (`HALF_UP`, escala 2) e valida operações entre moedas diferentes.

Decisões de domínio relevantes:

- **`CartaoDeCredito`** é o agregado que encapsula a regra de parcelamento: `registrarCompra(...)` decide em qual fatura a compra cai (comparando o dia da compra com `diaFechamento`) e distribui as parcelas nas faturas seguintes (uma por mês), com a última parcela absorvendo o resto do arredondamento da divisão.
- **`Fatura`** tem uma máquina de estados simples (`ABERTA → FECHADA → PAGA`) que impede novos lançamentos em faturas já fechadas (`FaturaFechadaException`).
- O domínio depende de **interfaces** de repositório (`ContaRepositorio`, `CartaoRepositorio`); isso deixa a porta pronta para, no futuro, plugar uma fonte de dados vinda do Open Finance sem tocar no domínio.

## Ambiente de desenvolvimento local

- Pré-requisitos: JDK 25, Docker (`./mvnw` é o Maven Wrapper, não precisa Maven instalado).
- **VS Code**: `code .` sugere as extensões de `.vscode/extensions.json`; F5 roda `FinancasPessoaisApplication` (`.vscode/launch.json`, sobe o Postgres antes); tasks em `.vscode/tasks.json`; SQLTools já configurado (`.vscode/settings.json`) apontando para o Postgres do `docker-compose.yml`.
- **IntelliJ**: abrir o `pom.xml` na raiz basta; `.idea/` não é versionado.
- **API manual**: coleção do Bruno em `bruno/` (ambiente **Local** define `baseUrl`); rodar primeiro `Criar Cartao`/`Criar Conta` — um script pós-resposta guarda o `id` numa variável usada pelos demais requests da mesma pasta.
