package com.calderon.pruebatecnica.dto;

public class MensajeDTO {
    private String IDMensaje;

    public MensajeDTO() {
    }

    public MensajeDTO(String idMensaje) {
        this.IDMensaje = idMensaje;
    }

    public String getIDMensaje() {
        return IDMensaje;
    }

    public void setIDMensaje(String iDMensaje) {
        this.IDMensaje = iDMensaje;
    }
}
