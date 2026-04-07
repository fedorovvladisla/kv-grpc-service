package com.example.kv.grpc;

import com.example.kv.repository.KVRepository;
import com.example.kv.KvProto.*;
import com.example.kv.KvServiceGrpc;
import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;

@GrpcService
@RequiredArgsConstructor
public class KVGrpcService extends KvServiceGrpc.KvServiceImplBase {

    private final KVRepository kvRepository;

    @Override
    public void put(PutRequest request, StreamObserver<PutResponse> responseObserver) {
        try {
            byte[] value = request.getValue().toByteArray();
            boolean isNull = request.getIsNull();
            kvRepository.put(request.getKey(), value, isNull);
            responseObserver.onNext(PutResponse.newBuilder().setSuccess(true).build());
        } catch (Exception e) {
            e.printStackTrace();
            responseObserver.onNext(PutResponse.newBuilder().setSuccess(false).build());
        }
        responseObserver.onCompleted();
    }

    @Override
    public void get(GetRequest request, StreamObserver<GetResponse> responseObserver) {
        try {
            List<?> result = kvRepository.get(request.getKey());
            if (result != null && !result.isEmpty()) {
                Object raw = result.get(0);
                if (raw instanceof List) {
                    List<?> tuple = (List<?>) raw;
                    Object valueObj = tuple.size() > 1 ? tuple.get(1) : null;
                    boolean isNull = (valueObj == null);
                    GetResponse.Builder builder = GetResponse.newBuilder()
                            .setFound(true)
                            .setIsNull(isNull);
                    if (!isNull && valueObj instanceof byte[]) {
                        builder.setValue(ByteString.copyFrom((byte[]) valueObj));
                    }
                    responseObserver.onNext(builder.build());
                } else {
                    responseObserver.onNext(GetResponse.newBuilder().setFound(false).build());
                }
            } else {
                responseObserver.onNext(GetResponse.newBuilder().setFound(false).build());
            }
        } catch (Exception e) {
            responseObserver.onNext(GetResponse.newBuilder().setFound(false).build());
        }
        responseObserver.onCompleted();
    }

    @Override
    public void delete(DeleteRequest request, StreamObserver<DeleteResponse> responseObserver) {
        try {
            kvRepository.delete(request.getKey());
            responseObserver.onNext(DeleteResponse.newBuilder().setSuccess(true).build());
        } catch (Exception e) {
            responseObserver.onNext(DeleteResponse.newBuilder().setSuccess(false).build());
        }
        responseObserver.onCompleted();
    }

    @Override
    public void range(RangeRequest request, StreamObserver<KvPair> responseObserver) {
        try {
            String since = request.getKeySince();
            String to = request.getKeyTo();
            List<List<Object>> tuples = kvRepository.range(since, to);
            for (List<Object> tuple : tuples) {
                if (tuple.size() >= 1) {
                    String key = (String) tuple.get(0);
                    Object valueObj = tuple.size() > 1 ? tuple.get(1) : null;
                    boolean isNull = (valueObj == null);
                    KvPair.Builder builder = KvPair.newBuilder()
                            .setKey(key)
                            .setIsNull(isNull);
                    if (!isNull && valueObj instanceof byte[]) {
                        builder.setValue(ByteString.copyFrom((byte[]) valueObj));
                    }
                    responseObserver.onNext(builder.build());
                }
            }
        } catch (Exception e) {
        }
        responseObserver.onCompleted();
    }

    @Override
    public void count(Empty request, StreamObserver<CountResponse> responseObserver) {
        try {
            long count = kvRepository.count();
            responseObserver.onNext(CountResponse.newBuilder().setCount(count).build());
        } catch (Exception e) {
            responseObserver.onNext(CountResponse.newBuilder().setCount(0).build());
        }
        responseObserver.onCompleted();
    }
}