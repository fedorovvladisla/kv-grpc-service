//package com.example.kv.entity;
//
//import com.fasterxml.jackson.annotation.JsonFormat;
//import com.fasterxml.jackson.annotation.JsonProperty;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.tarantool.core.mapping.Field;
//import org.springframework.data.tarantool.core.mapping.Tuple;
//
//@JsonFormat(shape = JsonFormat.Shape.ARRAY)
//@Tuple("KV")
//public class KV {
//
//    @Id
//    @JsonProperty("key")
//    private String key;
//
//    @Field("value")
//    @JsonProperty("value")
//    private byte[] value;
//
//    public KV() {}
//
//    public KV(String key, byte[] value) {
//        this.key = key;
//        this.value = value;
//    }
//
//    public String getKey() { return key; }
//    public void setKey(String key) { this.key = key; }
//    public byte[] getValue() { return value; }
//    public void setValue(byte[] value) { this.value = value; }
//}