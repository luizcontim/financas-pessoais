# 0001 — Núcleo transacional (contas e cartões de crédito)

**Status:** implementado
**Data:** 2026-08-22 (spec escrita retroativamente, documentando a decisão original)

## Contexto

Primeiro incremento do `financas-pessoais`: um sistema de gestão financeira
pessoal para registrar entradas/saídas em contas e compras (inclusive
parceladas) em cartões de crédito. O objetivo era ter um domínio real e rico o
suficiente para praticar DDD e arquitetura limpa — não um MVP mínimo qualquer,
mas um com pelo menos um agregado com invariantes de negócio de verdade (o
parcelamento de compras distribuído entre faturas).

Decisão deliberada de escopo: autenticação, dashboard e integração com Open
Finance ficaram fora deste incremento (ver seção "Fora de escopo").

## Requisitos

- [x] Criar uma conta e registrar entradas/saídas nela
- [x] Consultar o extrato de uma conta (saldo + movimentações)
- [x] Emitir um cartão de crédito (nome, bandeira, limite, dia de fechamento, dia de vencimento)
- [x] Registrar uma compra no cartão, à vista ou parcelada, e ver as parcelas caírem automaticamente nas faturas corretas
- [x] Consultar uma fatura específica (mês/ano) com suas parcelas e valor total
- [x] Fechar uma fatura, impedindo novos lançamentos nela

## Casos de uso

**Caso de uso: Registrar compra parcelada**
- **Dado** um cartão de crédito com dia de fechamento = 10
- **Quando** uma compra de R$ 100 em 3x é registrada com data 05/08
- **Então** a 1ª parcela cai na fatura de agosto (compra antes do fechamento), a 2ª em setembro, a 3ª em outubro — cada uma de R$ 33,33, exceto a última (R$ 33,34), que absorve o resto do arredondamento

**Caso de uso: Fechar fatura**
- **Dado** uma fatura com status `ABERTA`
- **Quando** o fechamento é solicitado
- **Então** o status muda para `FECHADA` e novas tentativas de adicionar parcela (ou fechar de novo) retornam erro (`409 Conflict`)

## Fora de escopo

- **Autenticação/multiusuário** — sistema é single-user por enquanto
- **Dashboard/consultas agregadas** — só CRUD transacional, sem visão de gasto por categoria/mês ainda
- **Integração com Open Finance** — de propósito adiada: é a parte de maior custo/complexidade (credenciamento, mTLS, consentimento) e ensina pouco sobre DDD em si
- **Pagamento de fatura** — existe o estado `PAGA` no enum `StatusFatura`, mas nenhum caso de uso ainda transiciona pra ele

## Decisões técnicas

- **`Dinheiro` como Value Object** (`dominio/modelo/Dinheiro.java`) em vez de `BigDecimal` solto: centraliza arredondamento (2 casas, `HALF_UP`) e impede somar valores de moedas diferentes — evita primitive obsession.
- **A regra de parcelamento vive no agregado `CartaoDeCredito`** (`registrarCompra(...)`), não num service anêmico: decidir em qual fatura a 1ª parcela cai (comparando o dia da compra com o dia de fechamento) e distribuir o resto do valor (absorvendo arredondamento na última parcela) são invariantes do próprio cartão, não lógica de aplicação.
- **`Fatura` tem máquina de estados** (`ABERTA → FECHADA → PAGA`): a transição é validada dentro da própria entidade (`Fatura.fechar()`), lançando `FaturaFechadaException` se inválida — a regra não pode ser burlada por quem chama de fora.
- **Casos de uso sem anotação do Spring**: `aplicacao/casodeuso/*UseCase.java` são classes Java simples, testáveis sem subir `ApplicationContext`. A fiação como beans do Spring é isolada em `config/CasoDeUsoConfig.java`.
- **Repositórios como portas** (`dominio/repositorio/ContaRepositorio`, `CartaoRepositorio`): o domínio não conhece JPA. As implementações concretas (`infraestrutura/persistencia/*RepositorioJpa.java`) mapeiam entre o modelo de domínio e entidades JPA via mappers dedicados (`ContaMapper`, `CartaoCreditoMapper`) — nunca reaproveitando a entidade JPA como se fosse o modelo de domínio.
- **IDs como UUID**, gerados no próprio domínio (`UUID.randomUUID()`), não no banco: o agregado nasce com identidade própria antes de ser persistido, o que facilita testes de domínio puro (sem precisar de um `id` gerado pelo banco pra existir).

## Referências

- Código: `src/main/java/com/luizcontim/financas/dominio/modelo/{CartaoDeCredito,Fatura,Conta,Dinheiro}.java`
- Testes: `src/test/java/com/luizcontim/financas/dominio/modelo/CartaoDeCreditoTest.java`
- Vault Obsidian: `[[Domain-Driven Design (DDD)]]`, `[[Arquitetura Limpa]]`
