package com.calderon.pruebatecnica.models;

import java.util.List;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "Inventario")
@ToString(exclude = { "polizas" })
@Getter
@Setter
@EqualsAndHashCode

public class Inventario {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "Sku", updatable = false, nullable = false, length = 36)

    private String Sku;

    // Campo: Nombre VARCHAR(100) NOT NULL
    // @Column(name = "Nombre") Es solo cuando no coincide con como esta nombrado el
    // campo en la bd

    private String Nombre;

    // Campo: Cantidad int NOT NULL
    private Integer Cantidad;

    @OneToMany(mappedBy = "inventario")
    @JsonIgnore
    private List<Poliza> polizas;
}
