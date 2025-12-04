package com.calderon.pruebatecnica.dto;

public class ApiResponse<T> {
    private Meta Meta;
    private T Data;

    public ApiResponse() {
    }

    public ApiResponse(Meta meta, T data) {
        this.Meta = meta;
        this.Data = data;
    }

    public Meta getMeta() {
        return Meta;
    }

    public void setMeta(Meta meta) {
        this.Meta = meta;
    }

    public T getData() {
        return Data;
    }

    public void setData(T data) {
        this.Data = data;
    }
}
