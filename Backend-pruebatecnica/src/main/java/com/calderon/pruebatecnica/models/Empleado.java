package com.calderon.pruebatecnica.models;

import java.util.List;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "Empleado")
@ToString(exclude = { "polizas" })
@Getter
@Setter
@EqualsAndHashCode

public class Empleado {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "IdEmpleado", updatable = false, nullable = false, length = 36)
    private String IdEmpleado;

    // Campo: Nombre VARCHAR(100) NOT NULL
    // @Column(name = "Nombre") Es solo cuando no coincide con como esta nombrado el
    // campo en la bd
    private String Nombre;

    // Campo: Apellido VARCHAR(100) NOT NULL
    private String Apellido;

    // Campo: Puesto VARCHAR(50)
    private String Puesto;

    @OneToMany(mappedBy = "empleado")
    @JsonIgnore
    private List<Poliza> polizas;
}
