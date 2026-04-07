package com.example.kv.repository;

import io.tarantool.client.TarantoolClient;
import io.tarantool.mapping.TarantoolResponse;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Repository
public class KVRepository {

    private final TarantoolClient client;

    public KVRepository(TarantoolClient client) {
        this.client = client;
    }

    public void put(String key, byte[] value, boolean isNull) throws ExecutionException, InterruptedException {
        if (isNull) {
            client.eval("box.space.KV:replace({...})", Arrays.asList(key, null)).get();
        } else {
            client.eval("box.space.KV:replace({...})", Arrays.asList(key, value)).get();
        }
    }

    public List<?> get(String key) throws ExecutionException, InterruptedException {
        TarantoolResponse<List<?>> response = client.eval(
                "return box.space.KV:get({...})",
                Arrays.asList(key)
        ).get();
        return response.get();
    }

    public void delete(String key) throws ExecutionException, InterruptedException {
        client.eval("box.space.KV:delete({...})", Arrays.asList(key)).get();
    }

    public List<List<Object>> range(String keySince, String keyTo) throws ExecutionException, InterruptedException {
        TarantoolResponse<List<?>> response = client.eval(
                "local result = {} " +
                        "local start = ... " +
                        "local finish = select(2, ...) " +
                        "for _, tuple in box.space.KV.index.primary:pairs(start, {iterator = box.index.GE}) do " +
                        "    if tuple[1] > finish then break end " +
                        "    table.insert(result, tuple) " +
                        "end " +
                        "return result",
                Arrays.asList(keySince, keyTo)
        ).get();
        List<?> raw = response.get();
        if (raw == null) return Collections.emptyList();
        List<List<Object>> typed = new ArrayList<>();
        for (Object obj : raw) {
            if (obj instanceof List) {
                typed.add((List<Object>) obj);
            }
        }
        return typed;
    }

    public long count() throws ExecutionException, InterruptedException {
        TarantoolResponse<List<?>> response = client.eval("return box.space.KV:len()").get();
        List<?> result = response.get();
        return result != null && !result.isEmpty() ? ((Number) result.get(0)).longValue() : 0L;
    }
}