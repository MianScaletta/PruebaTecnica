package com.calderon.pruebatecnica.dto;

public class Meta {
    private String Status;

    public Meta() {
    }

    public Meta(String status) {
        this.Status = status;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        this.Status = status;
    }
}
