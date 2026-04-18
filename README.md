# CultivaClub - Versão Monolítica

Projeto unificado que consolida em uma única aplicação Spring Boot o que
anteriormente estava dividido em **MS-1-Usuarios**, **MS-2-Objetos** e
**Gateway-CultivaClub**.

## Estado atual

Este monólito está na fase de estruturação. Todo o código dos microsserviços
originais foi adaptado e reorganizado, **mas nenhum banco de dados está
conectado neste momento**. A autoconfiguração de `DataSource` e JPA está
desabilitada em `application.yaml` para permitir que o projeto compile e
evolua sem dependência de infraestrutura externa.

## Estrutura de pacotes

```
com.cultivaclub.monolito
├── CultivaClubApplication.java         # entry point Spring Boot
│
├── common/                             # componentes compartilhados
│   ├── config/
│   │   ├── CorsConfig.java             # unificado do MS-1 + Gateway
│   │   └── SecurityConfiguration.java  # unificado do MS-1 + MS-2
│   ├── exception/
│   │   ├── OperacaoNaoPermitida.java
│   │   ├── RecursoNaoEncontrado.java
│   │   └── RegistroDuplicado.java
│   └── web/
│       ├── GlobalExceptionHandler.java # handler único do monólito
│       └── dto/
│           ├── ErroCampoDTO.java
│           └── ErroRespostaDTO.java
│
├── usuarios/                           # domínio de Usuários (ex-MS-1)
│   ├── domain/
│   │   ├── Usuario.java
│   │   └── ROLES.java
│   ├── repository/
│   │   └── UsuarioRepository.java
│   ├── service/
│   │   └── UsuarioService.java
│   ├── validator/
│   │   └── ValidatorUsuario.java
│   └── web/
│       ├── UsuarioController.java      # /cadastro
│       ├── LoginController.java        # /login
│       ├── dto/
│       │   ├── UsuarioDTO.java
│       │   ├── LoginDTO.java
│       │   ├── LoginRespostaDTO.java
│       │   ├── AtualizarRoleDTO.java
│       │   └── ResultadoPesquisaDeUsuarioDTO.java
│       └── mapper/
│           └── UsuarioMapper.java
│
└── objetos/                            # domínio de Objetos (ex-MS-2)
    ├── domain/
    │   ├── Cards.java
    │   ├── Tarefas.java
    │   └── TIPO_ALIMENTOS.java
    ├── repository/
    │   ├── CardsRepository.java
    │   ├── TarefasRepository.java
    │   └── spec/
    │       └── CardsSpec.java
    ├── service/
    │   └── CardsService.java
    ├── validator/
    │   └── ValidadorCardsPro.java      # antes: validatorPRO
    └── web/
        ├── CardsController.java        # /api/cards
        ├── dto/
        │   ├── CardDTO.java
        │   ├── CardRespostaDTO.java
        │   ├── TarefaDTO.java
        │   ├── TarefaRespostaDTO.java
        │   └── ResultadoDePesquisaCardsDTO.java
        └── mapper/
            └── CardMapper.java
```

## Principais decisões de arquitetura

- **Separação por domínio.** Dois módulos principais (`usuarios` e `objetos`)
  que espelham os microsserviços originais, mas convivem em um único deploy.
  Cada módulo possui suas próprias camadas `domain`, `repository`, `service`,
  `validator` e `web` (controller + dto + mapper). Nada de vazamento entre
  domínios: a comunicação é feita via **interface de serviço**
  (`UsuarioService`), não por acesso direto ao repositório alheio.

- **Common para cross-cutting.** Exceções de negócio, DTOs de erro,
  `GlobalExceptionHandler`, `CorsConfig` e `SecurityConfiguration` ficam em
  `common`, evitando duplicação existente antes entre MS-1 e MS-2.

- **Sem mensageria.** O `UsuarioEventPublisher`, o `UsuarioEventListener`,
  as filas RabbitMQ e a tabela `UsuarioRoleCache` foram removidos. Eles só
  existiam para comunicação entre serviços. No monólito o
  `ValidadorCardsPro` consulta o `UsuarioService` diretamente para checar
  se o usuário é Pro.

- **Sem gateway.** As rotas `/cadastro`, `/login` e `/api/cards` passam a
  ser servidas pela própria aplicação. O CORS é configurado diretamente
  contra o frontend.

- **JPA mantida, mas dormente.** As entidades, repositórios e
  `@EnableJpaAuditing` continuam no projeto, prontos para quando o Postgres
  for plugado. Enquanto isso, `DataSourceAutoConfiguration` e
  `HibernateJpaAutoConfiguration` estão excluídos no `application.yaml`.

## Endpoints (herdados dos microsserviços)

Usuários (ex-MS-1):
- `POST /cadastro` - criar usuário
- `GET /cadastro` - listar usuários
- `GET /cadastro/{id}` - buscar por id
- `PUT /cadastro/{id}` - atualizar
- `PATCH /cadastro/{id}/role` - atualizar role
- `DELETE /cadastro/{id}` - remover
- `POST /login` - autenticar

Objetos (ex-MS-2):
- `POST /api/cards` - criar card
- `GET /api/cards` - pesquisar (paginado)
- `GET /api/cards/{id}` - buscar por id
- `PUT /api/cards/{id}` - atualizar
- `DELETE /api/cards/{id}` - remover
- `POST /api/cards/{cardId}/tarefas` - criar tarefa
- `PUT /api/cards/{cardId}/tarefas/{tarefaId}` - atualizar tarefa
- `DELETE /api/cards/{cardId}/tarefas/{tarefaId}` - remover tarefa
- `PATCH /api/cards/{cardId}/tarefas/{tarefaId}/concluir` - alternar conclusão
- `GET /api/cards/tarefas/calendario` - tarefas por data

## Próximos passos sugeridos

1. Plugar Postgres: remover `spring.autoconfigure.exclude` e adicionar
   as propriedades de `spring.datasource` em `application.yaml`.
2. Substituir o token UUID devolvido pelo `LoginController` por JWT real
   (as dependências `jjwt-*` já estão no `pom.xml`).
3. Proteger os endpoints de `/api/cards` e `/cadastro` por autenticação
   em `SecurityConfiguration` assim que o JWT estiver em uso.
4. Criar testes de integração cobrindo os fluxos de criação de usuário
   e de card com validação de limites.
