---
name: revisao-ddd
description: Revisa mudanças recentes no financas-pessoais contra as regras de DDD e Arquitetura Limpa específicas deste projeto (isolamento do domínio, Value Objects, invariantes em agregados, fronteiras de camada). Use depois de implementar ou alterar código de domínio/aplicação.
---

# Revisão DDD / Arquitetura Limpa — financas-pessoais

Você vai revisar o diff atual (ou os arquivos indicados pelo usuário) contra
as regras arquiteturais deste projeto específico, documentadas em
`README.md` e em `docs/specs/0001-nucleo-transacional.md`. Não é uma revisão
genérica — é checar aderência ao que este projeto já decidiu.

## Checklist

1. **Isolamento do domínio** — nada em `dominio/` importa `org.springframework.*`,
   `jakarta.persistence.*` nem qualquer classe de `infraestrutura/`. Se algum
   arquivo em `dominio/modelo` ou `dominio/repositorio` violar isso, é o
   achado mais grave possível — aponte a linha exata.

2. **Primitive obsession** — dinheiro sempre como `Dinheiro` (nunca
   `BigDecimal`/`double` soltos representando valor monetário em código de
   domínio ou aplicação); outros conceitos do domínio com regras próprias
   (não só um rótulo) merecem VO também, não `String`/`int` cru.

3. **Invariantes no agregado certo** — regras de negócio (ex.: "não pode
   fechar fatura duas vezes", "parcela cai em qual fatura") devem estar
   dentro do agregado (`CartaoDeCredito`, `Conta`, `Fatura`), não vazadas
   para um `UseCase` ou, pior, para o controller. Se encontrar um `UseCase`
   decidindo regra de negócio em vez de só orquestrar (buscar → chamar método
   do domínio → salvar), aponte.

4. **Casos de uso sem framework** — classes em `aplicacao/casodeuso/` não
   devem ter anotações do Spring (`@Service`, `@Component`, etc.) nem
   depender de nada de `infraestrutura/`. A fiação como bean é
   responsabilidade exclusiva de `config/CasoDeUsoConfig.java`.

5. **Repositórios como porta** — o domínio depende só das interfaces em
   `dominio/repositorio/`. As implementações JPA nunca devem reaproveitar a
   entidade `*JpaEntity` como se fosse o modelo de domínio — sempre passando
   pelos mappers dedicados (`ContaMapper`, `CartaoCreditoMapper`).

6. **Fronteira DTO** — objetos de `aplicacao/dto/` e
   `infraestrutura/web/dto/` usam tipos primitivos/`BigDecimal`/`String`, não
   tipos de domínio (`Dinheiro`, `Categoria` tudo bem como enum, mas nunca
   `CartaoDeCredito` inteiro atravessando a fronteira web).

7. **Spec correspondente** — se a mudança é uma feature nova (não um bugfix
   pontual), existe uma spec em `docs/specs/` cobrindo ela? Se não, sinalize
   antes de aprovar — é o processo Spec-Driven definido no README.

## Como reportar

Liste os achados por gravidade (bloqueante > importante > sugestão), cada um
com arquivo:linha, o que está errado, e uma sugestão concreta de correção —
não só apontar o problema. Se nada foi encontrado, diga isso explicitamente
e destaque o que foi bem aplicado (reforça o padrão certo pro usuário).
