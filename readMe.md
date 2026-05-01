# How-to: Docker + Oracle XE + Quarkus

Guia passo a passo para subir o ambiente completo no linux (banco + API).

---

## Clonar os repositórios

Utilize o git para clonar os repositórios do código fonte em java e do script de inicialização da tabela para o banco de dados no HOME:

```bash
git clone https://github.com/nfreitas2000/nfreitas2000-CP2_Docker_SQL_Script.git

git clone https://github.com/nfreitas2000/CP2_Docker_CodigoFonte_Java.git
```

## Criar rede Docker

Crie uma rede docker para permitir comunicação entre os containers:

```bash
docker network create CP2_network
```

## Baixar imagem do Oracle XE

Baixe a imagem Oracle-exe (versão 21-slim-faststart) para a utilização do banco de dados:

```bash
docker pull gvenzl/oracle-xe:21-slim-faststart
```

## Subir o banco Oracle

Acesse a pasta do repositório do banco de dados:

```bash
cd nfreitas2000-CP2_Docker_SQL_Script/
```

E execute o container:

```bash
docker run -d \
  --name rm564992-containerOracle \
  --network CP2_network \
  -e ORACLE_PASSWORD=senha123 \
  -e APP_USER=dimdim \
  -e APP_USER_PASSWORD=dimdim123 \
  -p 1521:1521 \
  -v CP2_oracle_db:/opt/oracle/oradata \
  -v "$(pwd)/docker-entrypoint-init/init.sql":/container-entrypoint-initdb.d/init.sql \
  gvenzl/oracle-xe:21-slim-faststart
```

### Esperar o banco inicializar

Após a execução do container, veja os logs dele para confirmar se o banco de dados está em produção:

```bash
docker logs -f rm564992-containerOracle 2>&1 | grep -m1 "DATABASE IS READY TO USE"
```

### Caso ocorra erro no script SQL

Em caso de erros no script SQL (como não ter criado a tabela), entre no container:

```bash
docker exec -it rm564992-containerOracle bash
```

Conecte-se no banco:

```bash
sqlplus dimdim/dimdim123@//localhost:1521/XEPDB1
```

E crie a tabela de forma manual:

```bash
CREATE TABLE T_LUTAN_PRODUTOS (
    ID NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    NOME VARCHAR2(100 BYTE) NOT NULL,
    PRECO NUMBER NOT NULL,
    QUANTIDADE NUMBER NOT NULL
);
```

## Build da aplicação Quarkus

Ir para o projeto Java (repositório clonado):

```bash
cd
cd CP2_Docker_CodigoFonte_Java/code-with-quarkus/
```

## Build com Maven via Docker:

Execute o Maven por meio do docker para a execução do projeto:

```bash
docker run --rm \
  -v "$PWD":/app \
  -w /app \
  maven:3.9-eclipse-temurin-21 \
  mvn clean package
```

## Subir API Quarkus

Crie o container da API:

```bash
docker run -d \
  --name rm564992-quarkus-api \
  --network CP2_network \
  -p 8080:8080 \
  -v "$(pwd)/target/quarkus-app":/app \
  -w /app \
  -e QUARKUS_DATASOURCE_JDBC_URL=jdbc:oracle:thin:@rm564992-containerOracle:1521/XEPDB1 \
  -e QUARKUS_DATASOURCE_USERNAME=dimdim \
  -e QUARKUS_DATASOURCE_PASSWORD=dimdim123 \
  eclipse-temurin:21-jdk-jammy \
  java -jar quarkus-run.jar
```

## Testar a aplicação

Para realizar o teste do funcionamento, utilize no terminar:

```bash
curl -X GET http://localhost:8080/produto/read
```

Ou, utilizando uma ferramenta de gerenciamento de APIs (como o Postman), utlize o IP da Vm (20.43.20.228)

```bash
http://20.43.20.228:8080/produto/read
```

## EndPoits do projeto:

Para a realização do CRUD da tabela, utilize os seguintes endpoints (com o json, nos que necessitarem, no formato apresentado):

### CREATE

```bash
http://20.43.20.228:8080/produto/inserir

{
    "nome": "",
    "preco": 00.00,
    "quantidade": 0
}
```

### READ

```bash
http://20.43.20.228:8080/produto/read
```

### UPDATE

```bash
http://20.43.20.228:8080/produto/atualizar

{
    "id" : 0,
    "nome": "",
    "preco": 00.00,
    "quantidade": 0
}
```

### DELETE

```bash
http://20.43.20.228:8080/produto/deletar/{id}
```
