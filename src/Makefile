DOCKER_COMPOSE = docker compose
BACKEND_DIR = ./backend
FRONTEND_DIR = ./frontend

.PHONY: all backend test-backend frontend docker-up docker-down

all: docker-up backend frontend

backend:
	cd $(BACKEND_DIR) && mvn clean spring-boot:run

test-backend:
	cd $(BACKEND_DIR) && mvn test

frontend:
	cd $(FRONTEND_DIR) && npm i && npm run dev

docker-up:
	$(DOCKER_COMPOSE) up -d

docker-down:
	$(DOCKER_COMPOSE) down

# Dev commands
prettier:
	npx prettier --write .

psql-populate:
	psql -h localhost -p 7654 -U postgres -d deidb -f populate.sql

pg-dump:
	pg_dump -h localhost -p 7654 -U postgres -d deidb > populate.sql