# Desafio Votação

API REST para gerenciar sessões de votação em assembleias de cooperativas. Permite cadastrar pautas, abrir sessões de votação por tempo determinado, receber votos (Sim/Não) e apurar o resultado.

## Tecnologias

- **Java 21**
- **Spring Boot 3.x** (Spring Web, Spring Data JPA, Validation)
- **H2** em modo arquivo (persistência sem dependência externa)
- **Maven** (com wrapper incluído)
- **Lombok**
- **JUnit 5 + Mockito** (testes)

## Pré-requisitos

- **Java 21** instalado (`java -version` deve indicar 21)
- Não é necessário instalar Maven — o projeto inclui o Maven Wrapper (`mvnw`)
- Não é necessário banco de dados externo — o H2 grava em arquivo local

## Como executar

Na raiz do projeto:

```bash
# Linux/Mac
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

A aplicação sobe em `http://localhost:8080`.

Alternativamente, para gerar o `.jar` e executar:

```bash
./mvnw clean package
java -jar target/desafio-votacao-0.0.1-SNAPSHOT.jar
```

## Como rodar os testes

```bash
./mvnw test
```

## Persistência

O banco H2 grava em arquivo (`./data/votacao`), então **os dados sobrevivem ao restart** da aplicação, conforme exigido.

### Console do H2

Com a aplicação rodando, acesse `http://localhost:8080/h2-console` e use:

- **JDBC URL:** `jdbc:h2:file:./data/votacao`
- **User:** `sa`
- **Password:** (em branco)

## Endpoints da API

Todas as rotas usam o prefixo de versão `/v1`.

### Pautas

| Método | Rota | Descrição |
| --- | --- | --- |
| `POST` | `/v1/pautas` | Cadastra uma nova pauta |
| `GET` | `/v1/pautas` | Lista as pautas |
| `PUT` | `/v1/pautas/{id}` | Atualiza uma pauta |

Exemplo de corpo (POST):

```json
{
  "title": "Aprovação do orçamento 2026",
  "description": "Votação sobre o orçamento anual"
}
```

### Sessões

| Método | Rota | Descrição |
| --- | --- | --- |
| `POST` | `/v1/pautas/{pautaId}/sessoes` | Abre uma sessão de votação para a pauta |
| `GET` | `/v1/pautas/{pautaId}/sessoes` | Consulta a sessão da pauta |

A duração é opcional via query param `minutes`. Se omitida, o padrão é **1 minuto**:

```
POST /v1/pautas/1/sessoes          # sessão de 1 minuto (default)
POST /v1/pautas/1/sessoes?minutes=5 # sessão de 5 minutos
```

### Votos

| Método | Rota | Descrição |
| --- | --- | --- |
| `POST` | `/v1/pautas/{pautaId}/votos` | Registra um voto na pauta |

Exemplo de corpo:

```json
{
  "cpf": "123.456.789-01",
  "opcaoVoto": "SIM"
}
```

Opções válidas: `SIM`, `NAO`.

### Resultado

| Método | Rota | Descrição |
| --- | --- | --- |
| `GET` | `/v1/pautas/{pautaId}/votos/resultado` | Apura e retorna o resultado da votação |

Exemplo de resposta:

```json
{
  "pautaId": 1,
  "totalSim": 42,
  "totalNao": 17,
  "totalVotos": 59,
  "statusSessao": "FINALIZADA",
  "resultadoVotacao": "APROVADA"
}
```

## Decisões de arquitetura

- **Organização em camadas** (`controller`, `service`, `repository`, `entity`, `dto`, `enums`, `exception`): separação clara de responsabilidades, mantendo o controller fino e a regra de negócio no service.
- **DTOs de entrada e saída**: as entidades JPA nunca são expostas diretamente na API.
- **H2 em modo arquivo**: escolhido para atender o requisito de persistência sem exigir que o avaliador suba um banco externo. A troca por PostgreSQL em produção seria apenas configuração, graças ao JPA. Em produção, o `ddl-auto` seria substituído por migrations (Flyway/Liquibase).
- **Tratamento de erros centralizado**: um `@RestControllerAdvice` traduz exceções de negócio em respostas HTTP consistentes (404 para recurso não encontrado, 409 para violação de regra de negócio, 400 para validação).

## Regras de negócio

- Uma pauta pode ter **uma única sessão** de votação.
- Uma sessão fica aberta pelo tempo definido na abertura (ou 1 minuto por padrão).
- Um associado (identificado por CPF) vota **apenas uma vez** por pauta.
- Só é possível votar em uma sessão **aberta**.
- O resultado pode ser consultado a qualquer momento; enquanto a sessão está aberta, reflete a contagem parcial (indicada pelo campo `statusSessao`).

## Versionamento da API (Tarefa Bônus 3)

A API é versionada por **URI** (prefixo `/v1` nas rotas). Optei por essa estratégia por ser a mais simples, explícita e legível — a versão fica visível na própria URL, facilitando testes e evitando ambiguidade. Para evoluções incompatíveis, uma nova versão (`/v2`) conviveria com a anterior sem quebrar clientes existentes.

## Testes automatizados

Os testes cobrem a camada de serviço (regra de negócio), usando JUnit 5 e Mockito para isolar as dependências:

- `PautaServiceTest`: CRUD de pautas, incluindo o caminho de recurso não encontrado.
- `VotoServiceTest`: registro de voto e as validações de negócio (sessão fechada, voto duplicado).