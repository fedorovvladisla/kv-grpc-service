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
grpcurl -plaintext -d "{\"key\":\"test\",\"value\":\"dGVzdA==\",\"is_null\":false}" localhost:9090 kv.KvService/Put
```
- Get (получить)
```bash
grpcurl -plaintext -d "{\"key\":\"test\"}" localhost:9090 kv.KvService/Get
```
- Count
```bash
grpcurl -plaintext -d "{}" localhost:9090 kv.KvService/Count
```
- Range
```bash
grpcurl -plaintext -d "{\"key_since\":\"a\",\"key_to\":\"z\"}" localhost:9090 kv.KvService/Range
```
- Delete
```bash
grpcurl -plaintext -d "{\"key\":\"test\"}" localhost:9090 kv.KvService/Delete
```
 
