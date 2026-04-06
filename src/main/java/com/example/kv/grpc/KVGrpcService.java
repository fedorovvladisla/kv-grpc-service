package com.example.kv.grpc;

import com.example.kv.KVServiceGrpc;

import com.example.kv.KvService.*;
import com.example.kv.service.KVStorageService;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class KVGrpcService extends KVServiceGrpc.KVServiceImplBase {

    private final KVStorageService storage;

    public KVGrpcService(KVStorageService storage) {
        this.storage = storage;
    }

    @Override
    public void put(KeyValue request, StreamObserver<Empty> responseObserver) {
        byte[] value = request.getValue().toByteArray();
        storage.put(request.getKey(), value.length == 0 ? null : value);
        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void get(Key request, StreamObserver<Value> responseObserver) {
        byte[] value = storage.get(request.getKey());
        Value response = Value.newBuilder()
                .setValue(value != null ? com.google.protobuf.ByteString.copyFrom(value) : com.google.protobuf.ByteString.EMPTY)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void delete(Key request, StreamObserver<Empty> responseObserver) {
        storage.delete(request.getKey());
        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void range(RangeRequest request, StreamObserver<KeyValue> responseObserver) {
        storage.rangeStream(request.getKeySince(), request.getKeyTo(), entry -> {
            KeyValue kv = KeyValue.newBuilder()
                    .setKey(entry.getKey())
                    .setValue(entry.getValue() != null ? com.google.protobuf.ByteString.copyFrom(entry.getValue()) : com.google.protobuf.ByteString.EMPTY)
                    .build();
            responseObserver.onNext(kv);
        });
        responseObserver.onCompleted();
    }

    @Override
    public void count(Empty request, StreamObserver<CountResponse> responseObserver) {
        long total = storage.count();
        responseObserver.onNext(CountResponse.newBuilder().setCount(total).build());
        responseObserver.onCompleted();
    }
}