package com.example.kv.service;

import io.tarantool.client.crud.TarantoolCrudClient;
import io.tarantool.client.factory.TarantoolCrudClientBuilder;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;
import java.util.AbstractMap;
import java.util.Map;
import java.util.function.Consumer;

@Service
public class KVStorageService {

    private final TarantoolCrudClient client;

    public KVStorageService(TarantoolCrudClientBuilder builder) throws Exception {
        this.client = builder.build();
    }

    public void put(String key, byte[] value) {
        client.call("kv_put", Arrays.asList(key, value)).join();
    }

    public byte[] get(String key) {
        List<?> result = (List<?>) client.call("kv_get", Arrays.asList(key)).join();
        return (result != null && !result.isEmpty() && result.get(0) instanceof byte[]) ? (byte[]) result.get(0) : null;
    }

    public void delete(String key) {
        client.call("kv_delete", Arrays.asList(key)).join();
    }

    public long count() {
        List<?> result = (List<?>) client.call("kv_count", Arrays.asList()).join();
        return (result != null && !result.isEmpty() && result.get(0) instanceof Number) ? ((Number) result.get(0)).longValue() : 0;
    }

    public void rangeStream(String keySince, String keyTo, Consumer<Map.Entry<String, byte[]>> consumer) {
        int limit = 1000;
        int offset = 0;
        boolean hasMore = true;

        while (hasMore) {
            List<?> page = (List<?>) client.call("select_range", Arrays.asList("KV", "primary", keySince, keyTo, limit, offset)).join();
            if (page == null || page.isEmpty()) {
                hasMore = false;
            } else {
                for (Object item : page) {
                    List<?> entry = (List<?>) item;
                    String currentKey = (String) entry.get(0);
                    byte[] value = (byte[]) entry.get(1);
                    consumer.accept(new AbstractMap.SimpleEntry<>(currentKey, value));
                }
                hasMore = page.size() == limit;
                offset += limit;
            }
        }
    }
}