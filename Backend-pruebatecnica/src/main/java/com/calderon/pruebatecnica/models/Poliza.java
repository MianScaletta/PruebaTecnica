package com.calderon.pruebatecnica.models;

import java.time.LocalDate;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "Polizas")
@ToString(exclude = { "empleado", "inventario" })
@Getter
@Setter
@JsonPropertyOrder({ "idPolizas" })
@EqualsAndHashCode

public class Poliza {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "idPolizas", updatable = false, nullable = false, length = 36)

    private String idPolizas;

    @Column(name = "EmpleadoGenero", insertable = false, updatable = false)
    private String EmpleadoGenero; // relacion con id Empleado

    @Column(name = "Sku", insertable = false, updatable = false)
    private String Sku; // relacion con sku inventario

    private Integer Cantidad;

    private LocalDate Fecha; // @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")

    @ManyToOne
    @JoinColumn(name = "EmpleadoGenero")
    @JsonIgnore
    private Empleado empleado;

    @ManyToOne
    @JoinColumn(name = "Sku")
    @JsonIgnore
    private Inventario inventario;
}
