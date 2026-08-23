---
name: spec-feature
description: Conduz o fluxo de Spec-Driven Development do financas-pessoais — cria a spec de uma feature nova a partir do template antes de liberar qualquer implementação. Use quando o usuário pedir uma feature nova do roadmap (autenticação, dashboard, observabilidade, Open Finance, etc.) ou disser algo como "vamos implementar X".
---

# Spec-Driven Development — financas-pessoais

Este projeto não começa a escrever código de uma feature nova sem antes ter
uma spec revisada em `docs/specs/`. Esta skill conduz esse fluxo.

## Passo 1 — Levantar contexto

Antes de escrever a spec, confirme com o usuário (ou verifique no
`README.md`/roadmap) o suficiente para preencher cada seção do template com
conteúdo real, não placeholder. Se algo estiver genuinamente em aberto,
pergunte — não invente requisito ou decisão técnica que o usuário não validou.

## Passo 2 — Criar a spec

Copie `docs/specs/TEMPLATE.md` para `docs/specs/000N-titulo-curto.md` (N =
próximo número sequencial livre; confira os arquivos existentes em
`docs/specs/` para não colidir). Preencha:

- **Contexto**: por que essa feature, por que agora
- **Requisitos**: lista objetiva, checável
- **Casos de uso**: no formato Dado/Quando/Então, cobrindo o caminho feliz e
  pelo menos um caso de borda relevante
- **Fora de escopo**: seja explícito sobre o que NÃO entra agora — isso evita
  escopo crescendo durante a implementação
- **Decisões técnicas**: preencha o que já for possível decidir de antemão
  (ex.: "vai precisar de um novo agregado X porque..."); tudo bem deixar
  itens em aberto pra decidir durante a implementação, mas marque como tal

## Passo 3 — Revisar completude antes de implementar

Antes de tocar em código, confira: todo requisito tem um caso de uso
correspondente? O "fora de escopo" é específico o suficiente pra evitar
ambiguidade depois? Apresente a spec ao usuário e peça confirmação explícita
antes de seguir para a implementação — não implemente silenciosamente a
partir de uma spec ainda não validada por ele.

## Passo 4 — Implementar referenciando a spec

Durante a implementação, mantenha a spec como fonte de verdade. Se a
implementação revelar que a spec estava incompleta ou errada em algum ponto,
**atualize a spec junto** (não deixe o código divergir do documento
silenciosamente) e marque o `Status` no topo do arquivo (`rascunho` →
`em implementação` → `implementado`) conforme o progresso.
