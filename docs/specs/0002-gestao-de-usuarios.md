# 0002 — Gestão de usuários (propriedade de contas e cartões)

**Status:** implementado
**Data:** 2026-09-07

## Contexto

O sistema hoje é single-user: `Conta` e `CartaoDeCredito` não têm dono, e
qualquer client que conheça o UUID de um recurso pode lê-lo ou alterá-lo. A
spec `0001-nucleo-transacional.md` já registrava isso como decisão deliberada
("Autenticação/multiusuário — sistema é single-user por enquanto").

Agora a experiência da aplicação deve ser centrada no usuário: cada usuário
pode ter um ou mais cartões de crédito e contas. Este incremento introduz o
agregado `Usuario` e o vínculo de propriedade com `Conta`/`CartaoDeCredito`,
com cadastro self-service. Autenticação/login fica para uma spec futura — aqui
só nasce o dono do recurso, não ainda quem pode agir em nome dele.

## Requisitos

- [x] Cadastrar um usuário (nome, email, senha) via endpoint público
- [x] Impedir cadastro com email já existente
- [x] Armazenar a senha sempre com hash, nunca em texto puro
- [x] Toda `Conta` e todo `CartaoDeCredito` pertence a exatamente um `Usuario` desde a criação
- [x] Listar as contas de um usuário
- [x] Listar os cartões de um usuário

## Casos de uso

**Caso de uso: Cadastrar usuário**
- **Dado** um email ainda não cadastrado
- **Quando** `POST /usuarios` é chamado com nome, email e senha
- **Então** o usuário é criado com a senha já em hash, e a resposta traz id/nome/email (nunca o hash)

**Caso de uso: Cadastro com email duplicado**
- **Dado** um usuário já cadastrado com email `ana@example.com`
- **Quando** `POST /usuarios` é chamado novamente com o mesmo email
- **Então** a API responde `409 Conflict` e nenhum novo usuário é criado

**Caso de uso: Criar conta vinculada a um usuário**
- **Dado** um usuário já cadastrado
- **Quando** `POST /contas` é chamado com `usuarioId` desse usuário
- **Então** a conta é criada pertencendo a ele, e aparece em `GET /usuarios/{id}/contas`

**Caso de uso: Criar conta com usuário inexistente**
- **Dado** um `usuarioId` que não existe
- **Quando** `POST /contas` é chamado com esse id
- **Então** a API responde `404 Not Found` e nenhuma conta é criada

## Fora de escopo

- **Login/autenticação** — não há `SecurityFilterChain`, token ou sessão; os endpoints continuam publicamente acessíveis. Mecanismo (JWT vs sessão) fica em aberto para a spec de autenticação.
- **Autorização** — nenhum endpoint verifica se quem chama é o dono do recurso (não há ainda como saber quem está chamando).
- **Edição/exclusão de usuário, troca ou recuperação de senha.**
- **Migração de dados legados** — assume-se banco de desenvolvimento sem `conta`/`cartao_credito` pré-existentes; a migration adiciona `usuario_id NOT NULL` diretamente.
- **Perfis/papéis** (admin vs usuário comum).

## Decisões técnicas

- **`Email` e `SenhaHash` como Value Objects** (`dominio/modelo/`), não `String` solto: `Email` valida formato no construtor; `SenhaHash` é um wrapper opaco do hash já calculado — o domínio nunca vê a senha em texto puro nem conhece o algoritmo de hash, evitando primitive obsession e mantendo `dominio/` livre de dependência de criptografia.
- **`CodificadorDeSenha` como port** (`dominio/`), implementado com BCrypt em `infraestrutura/seguranca/`: como o mecanismo de autenticação ainda não foi decidido, evitamos puxar o starter completo de Spring Security — só a dependência `spring-security-crypto` (que fornece `BCryptPasswordEncoder`).
- **Unicidade de email verificada no caso de uso, não no agregado**: é uma invariante que atravessa a coleção inteira de usuários (precisa consultar o repositório), então não pode viver dentro do agregado `Usuario` — segue o mesmo raciocínio de qualquer checagem de unicidade em DDD.
- **`Conta`/`CartaoDeCredito` guardam `usuarioId` como referência simples** (`UUID`), não o objeto `Usuario` inteiro — mesmo padrão já usado por `Parcela` (que guarda `compraId`), evitando acoplamento entre agregados.
- **IDs de `Usuario` como UUID gerado no domínio**, mesmo padrão dos demais agregados (`Usuario.registrar(...)`/`reconstruir(...)`).

## Referências

- Spec anterior: `docs/specs/0001-nucleo-transacional.md`
- Código: `src/main/java/com/luizcontim/financas/dominio/modelo/{Usuario,Email,SenhaHash}.java`, `dominio/servico/CodificadorDeSenha.java`
- Migration: `src/main/resources/db/migration/V2__criar_usuario_e_vinculo_propriedade.sql`
