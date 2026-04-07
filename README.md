# kv-grpc-service

gRPC сервис для KV хранилища на Tarantool
## Требования
- Java 17
- Maven
- Docker & Docker Compose
- Tarantool 3.2.*
- tarantool-client-1.5.0

## Быстрый старт

### 1. Запуск Tarantool
```bash
docker-compose up -d
```
Проверьте, что контейнер запущен:
```bash
docker logs tarantool-kv
```
2. Сборка Java-приложения
   Убедитесь, что в pom.xml указаны зависимости:
- io.tarantool:tarantool-client:1.5.0
- net.devh:grpc-spring-boot-starter:2.15.0.RELEASE
  
Соберите JAR:
```bash
mvn clean package
```

3. Запуск gRPC сервера

```bash
java -jar target/kv-service-0.0.1-SNAPSHOT.jar  
```

4. Тестирование через grpcurl

- Put (сохранить запись)
```bash
grpcurl -plaintext -d '{"key":"user1","value":"SGVsbG8="}' localhost:9090 com.example.kv.KVService/Put 
```
- Get (получить)
```bash
grpcurl -plaintext -d '{"key":"user1"}' localhost:9090 com.example.kv.KVService/Get
```
- Count
```bash
grpcurl -plaintext -d '{}' localhost:9090 com.example.kv.KVService/Count
```
- Range
```bash
grpcurl -plaintext -d '{"key_since":"a","key_to":"z"}' localhost:9090 com.example.kv.KVService/Range
```
- Delete
```bash
grpcurl -plaintext -d '{"key":"user1"}' localhost:9090 com.example.kv.KVService/Delete
```
 
