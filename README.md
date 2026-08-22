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

Fases futuras, deliberadamente fora deste primeiro incremento:

- **Autenticação/multiusuário** — hoje o sistema é single-user, sem login.
- **Dashboard** — consultas agregadas (gasto por categoria/mês) e um front-end (ou integração com uma ferramenta de BI).
- **Observabilidade** — já há Actuator básico; falta métricas customizadas (Micrometer), Prometheus/Grafana e tracing distribuído.
- **Open Finance** — um novo adapter implementando as portas de repositório/fonte de transações, para importar dados diretamente das instituições financeiras, sem alterar o domínio.
- **Separação em bounded contexts** — hoje é um único módulo (package-by-layer); dividir em módulos/contextos quando a complexidade justificar.
