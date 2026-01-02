## SQL指引

### 啟動postgres container (如果有已經存在的volume也是用這個指令)
- docker run --name my-postgres -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=mydb -p 5432:5432 -v /Users/daniel/Files/OrbStack-pv/postgres/my-postgres:/var/lib/postgresql/data -d postgres

### 進入postgres
- psql -U postgres

### 創建database
- CREATE DATABASE mydb;

### 進入database
- \c mydb

### 創建schema
- CREATE SCHEMA company;

### 查看DB相關資訊
- 列出所有DB
  - SELECT datname FROM pg_database;
- 列出所有schema
  - \dn

### list all procedure and function
- \df+