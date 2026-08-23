# Finanças Pessoais

Sistema de gestão financeira individual: contas (entradas/saídas) e cartões de
crédito (compras, parcelamento, faturas). Projeto pessoal de estudo para colocar
em prática, num domínio real e motivador, conceitos de **DDD**, **arquitetura
limpa/hexagonal**, **containerização** e **observabilidade**.

Este é o primeiro incremento: o núcleo transacional do domínio, exposto via API
REST, sem front-end e sem autenticação ainda (ver [Roadmap](#roadmap)).

## Arquitetura

O código segue arquitetura hexagonal (ports & adapters) com as regras de negócio
isoladas em `dominio`, sem nenhuma dependência de framework:

```
com.luizcontim.financas
├── dominio/           # entidades, agregados, value objects e ports — zero Spring aqui
├── aplicacao/          # casos de uso e DTOs de entrada/saída
├── infraestrutura/     # adapters: persistência (JPA) e web (REST)
└── config/              # fiação dos casos de uso como beans do Spring
```

Decisões relevantes:

- **`Dinheiro`** é um value object (não `BigDecimal`/`double` soltos pelo código), evitando primitive obsession e centralizando arredondamento/validação de moeda.
- **`CartaoDeCredito`** é o agregado que encapsula a regra de parcelamento: `registrarCompra(...)` decide em qual fatura a compra cai (considerando o dia de fechamento) e distribui as parcelas nas faturas seguintes, absorvendo o resto do arredondamento na última parcela.
- **`Fatura`** tem uma máquina de estados simples (`ABERTA → FECHADA → PAGA`) que impede novos lançamentos em faturas já fechadas.
- Os casos de uso (`aplicacao/casodeuso`) não têm anotações do Spring — são classes Java simples, testáveis sem subir contexto algum. A fiação como beans acontece em `config/CasoDeUsoConfig`.
- O domínio depende de **interfaces** de repositório (`ContaRepositorio`, `CartaoRepositorio`); as implementações concretas com Spring Data JPA vivem em `infraestrutura/persistencia`. Isso deixa a porta pronta para, no futuro, plugar uma fonte de dados vinda do Open Finance sem tocar no domínio.

## Como rodar localmente

Pré-requisitos: JDK 21, Docker.

```bash
# sobe o PostgreSQL
docker compose up -d

# roda a aplicação (usa o Maven Wrapper, não precisa ter o Maven instalado)
./mvnw spring-boot:run
```

A API sobe em `http://localhost:8080`. Health check: `GET /actuator/health`.

Alternativa 100% containerizada (útil para testar o `Dockerfile` de produção):

```bash
docker compose up -d
docker build -t financas-pessoais .
docker run --network host -e DB_HOST=localhost financas-pessoais
```

### Rodando os testes

```bash
./mvnw verify
```

Os testes de domínio (`dominio/modelo/*Test.java`) são JUnit puro, sem contexto
Spring. Os testes de integração (`*IT.java`) sobem um PostgreSQL real via
**Testcontainers** — não precisa ter Postgres rodando à parte para eles, mas
precisa do Docker ativo.

## Processo de desenvolvimento (Spec-Driven)

Toda feature nova nasce de uma **spec** — um documento curto escrito *antes* do
código, guardado em [`docs/specs/`](docs/specs/), a partir do
[`TEMPLATE.md`](docs/specs/TEMPLATE.md). A spec descreve contexto, requisitos,
casos de uso, o que fica de fora e as decisões técnicas — é o registro
permanente do "porquê" por trás de cada pedaço do sistema, não só do "o quê".
[`0001-nucleo-transacional.md`](docs/specs/0001-nucleo-transacional.md)
documenta (retroativamente) o que já está implementado.

Fluxo pra uma feature nova: escrever a spec → revisar se está completa → só
então implementar, referenciando a spec no código/commits quando fizer sentido.
A skill `.claude/skills/spec-feature` automatiza esse fluxo.

## Ambiente de desenvolvimento local

Editor recomendado: **VS Code**. Ao abrir o projeto (`code .`), ele sugere instalar
as extensões em `.vscode/extensions.json` (Java, Spring Boot Tools, EditorConfig,
YAML, SQLTools). O suporte a Lombok já vem embutido no Language Support for Java
(Red Hat) — não precisa de extensão à parte.

- **Debug**: F5 roda a configuração `FinancasPessoaisApplication` (`.vscode/launch.json`). Suba o Postgres antes (`docker compose up -d`).
- **Tasks** (Ctrl+Shift+P → "Tasks: Run Task"): subir/derrubar o Postgres, `mvnw verify`, `mvnw spring-boot:run` — ver `.vscode/tasks.json`.
- **Banco de dados na IDE**: a extensão SQLTools já vem pré-configurada (`.vscode/settings.json`) apontando para o Postgres do `docker-compose.yml` — abra o painel SQLTools na barra lateral para navegar nas tabelas.
- **Estilo de código**: `.editorconfig` na raiz formaliza o padrão já usado (tabs em `.java`, 2 espaços em YAML/JSON/Markdown).

### IntelliJ IDEA

Alternativa ao VS Code (os dois coexistem, use o que preferir no dia). Basta
**Open** o `pom.xml` na raiz do projeto — o IntelliJ detecta o Maven, o JDK 21
e baixa as dependências sozinho; suporte a Lombok já vem nativo desde a versão
2020.3, sem plugin extra. Configure uma Run Configuration para
`com.luizcontim.financas.FinancasPessoaisApplication` para rodar/debugar.
Não versionamos `.idea/` no repo (projeto Maven puro não precisa disso pra
abrir corretamente); a ferramenta de banco de dados fica só no VS Code via
SQLTools (o Database Tools nativo do IntelliJ é exclusivo da versão Ultimate).

### Testando a API com o Bruno

A coleção fica em [`bruno/`](bruno/), versionada junto do código. Para usar:

1. Abra o Bruno → **Open Collection** → selecione a pasta `bruno/` deste repositório.
2. Selecione o ambiente **Local** (canto superior direito) — define `baseUrl=http://localhost:8080`.
3. Com a aplicação rodando, rode primeiro **Cartoes → Criar Cartao** (ou **Contas → Criar Conta**): um script pós-resposta guarda o `id` retornado numa variável (`cartaoId`/`contaId`).
4. Os demais requests da mesma pasta (`Registrar Compra`, `Consultar Fatura`, `Fechar Fatura` / `Registrar Movimentacao`, `Consultar Extrato`) já usam essa variável na URL — não precisa copiar UUID manualmente.

## Exemplo de fluxo (via API)

```bash
# criar um cartão
curl -X POST localhost:8080/cartoes -H "Content-Type: application/json" -d '{
  "nome": "Nubank", "bandeira": "Mastercard", "limite": 5000,
  "diaFechamento": 10, "diaVencimento": 17
}'

# registrar uma compra parcelada em 3x
curl -X POST localhost:8080/cartoes/{id}/compras -H "Content-Type: application/json" -d '{
  "descricao": "Notebook", "valorTotal": 3000, "categoria": "OUTROS",
  "dataCompra": "2026-08-05", "quantidadeParcelas": 3
}'

# consultar a fatura de setembro/2026
curl localhost:8080/cartoes/{id}/faturas/2026/9
```

## Roadmap

Fases futuras, deliberadamente fora deste primeiro incremento. Cada uma delas
ganha sua spec em `docs/specs/` antes de começar a implementação:

- **Autenticação/multiusuário** — hoje o sistema é single-user, sem login.
- **Dashboard** — consultas agregadas (gasto por categoria/mês) e um front-end (ou integração com uma ferramenta de BI).
- **Observabilidade** — já há Actuator básico; falta métricas customizadas (Micrometer), Prometheus/Grafana e tracing distribuído.
- **Open Finance** — um novo adapter implementando as portas de repositório/fonte de transações, para importar dados diretamente das instituições financeiras, sem alterar o domínio.
- **Separação em bounded contexts** — hoje é um único módulo (package-by-layer); dividir em módulos/contextos quando a complexidade justificar.
