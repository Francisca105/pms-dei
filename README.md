# PMS: PhD Management System

## Visão Geral

O **DEI PhD Management System** (ou PMS) é uma plataforma interna do Departamento de Engenharia Informática (DEI) projetado para facilitar a gestão do processo de acompanhamento de estudantes de doutoramento. A plataforma permite a organização e controle dos diferentes fluxos (**workflows**) relacionados com as fases da tese e da defesa.

O sistema foi desenvolvido utilizando **Spring Boot** para o backend e **Vue.js** para o frontend.

### Funcionalidades do PMS
(implementação obrigatória)
- [X] Visualizar todas as pessoas do DEI numa tabela de consulta rápida, contendo pelo menos:
  - Nome
  - IST ID
  - Email
  - Tipo  
- [X] Adicionar novas pessoas ao sistema  
- [X] Atualizar e remover pessoas existentes  
- [X] Atribuir papéis (**roles**) no frontend:
  - Staff
  - Estudante
  - Professor
  - Coordenador
  - SC (admin)
- [X] Visualizar todos os alunos numa tabela de consulta rápida, contendo pelo menos: 
  - Nome
  - IST ID
  - Email
  - Estado do workflow de tese/defesa
- [X] Filtrar alunos por estado do workflow de tese/defesa ou outro campo relevante  
- [X] Visualizar a página individual de cada aluno, incluindo:
  - [X] Secção de informações pessoais  
  - [X] Workflow de tese  
  - [X] Workflow de defesa
- [X] Atualizar estados dos workflows conforme as etapas descritas  
- [X] Reverter estados dos workflows conforme as etapas descritas
  - Nota: Reverter os estados só é possível na página específica da tese que se quer alterar.
---
(implementação opcional)
- [X] Página de estatísticas dos dados do sistema (versão simples)
- [X] Logs detalhados de ações feitas no sistema
---
(extras)
- Criação de testes de domínio e serviço
- Documentação (página de criação de teses)

---

## Dependências

Antes de iniciar o projeto, certifique-se de que possui as seguintes dependências instaladas:

- **Java 17** ([Download](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html))
- **Maven** ([Download](https://maven.apache.org/download.cgi))
- **Node.js 14+** ([Download](https://nodejs.org/en/)) (Recomenda-se o uso do [Node Version Manager](https://github.com/nvm-sh/nvm))
- **Docker** ([Download](https://www.docker.com/))

Outras dependências são instaladas automaticamente:

- **Spring Boot** ([Documentação](https://spring.io/projects/spring-boot))
- **Vue.js** ([Documentação](https://vuejs.org/))

---

## Como Executar Localmente

### 1. Clonar o Projeto

```bash
git clone git@gitlab.rnl.tecnico.ulisboa.pt:<REPO>
```

Entre no diretório do projeto:

```bash
cd src/
```

### 2. Banco de Dados

Para rodar o banco de dados com **Docker** (recomendado):

```bash
docker compose up
```

Para executar os serviços em segundo plano:

```bash
docker compose up -d
```

Para parar o banco de dados:

```bash
docker compose down
```

Caso queira acessar o banco manualmente:

```bash
psql -h localhost -p <PORT> -U <USER> <DB_NAME>
```

Se estiver a usar o Docker Compose fornecido:

- **PORT** = `7654`
- **USER** = `postgres`
- **DB\_NAME** = `deidb`

### 3. Backend

Criar um arquivo de configuração local:

```bash
cp ./backend/src/main/resources/application.properties.example ./backend/src/main/resources/application.properties
```

Certifique-se de que as variáveis de conexão do banco de dados correspondem às definições do `docker-compose.yml`.

Para compilar e rodar o backend:

```bash
cd ./backend
mvn clean spring-boot:run
```

#### 3.1 Makefile

Como alternativa, poderá usar o Makefile para inicializar o Back-end:
```bash
make docker-up
make backend
```

### 4. Frontend

Criar um arquivo de configuração local:

```bash
cp ./frontend/example.env ./frontend/.env
```

Instalar dependências:

```bash
cd ./frontend
npm install
```

Executar o frontend:

```bash
npm run dev
```

O sistema estará disponível em [**http://localhost:5173/**](http://localhost:5173/) por padrão.

#### 3.1 Makefile

Como alternativa, poderá usar o Makefile para inicializar o Front-end:
```bash
make frontend
```

---

## Funcionalidades Principais

- **Gestão de Pessoas**: Cadastro, edição e remoção de usuários.
- **Gestão de Workflows**: Acompanhamento das fases da tese e defesa.
- **Papéis de Usuário**: Diferentes acessos conforme o tipo de usuário.
- **Filtros e Pesquisa**: Localização rápida de alunos e workflows.

---

## População do Banco de Dados

Para popular a base de dados com dados de teste, utilize o arquivo `populate.sql`. Para restaurar o dump:

```bash
psql -h localhost -p <PORT> -U <USER> -d <DB_NAME> -f populate.sql
```

- **PORT** = `7654`
- **USER** = `postgres`
- **DB\_NAME** = `deidb`


## Observações

- O projeto deve ser entregue no repositório GitLab privado fornecido.
- Certifique-se de utilizar `git` corretamente para versionamento.