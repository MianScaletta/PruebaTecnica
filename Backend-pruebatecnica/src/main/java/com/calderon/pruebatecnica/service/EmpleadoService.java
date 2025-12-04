package com.calderon.pruebatecnica.service;

import org.springframework.data.domain.Page;

import com.calderon.pruebatecnica.models.Empleado;

import java.util.List;
import java.util.Optional;

public interface EmpleadoService {

    public List<Empleado> findAll();

    public Empleado findById(String id);

    public Empleado save(Empleado empleado);

    public void deleteById(String id);

    public Empleado update(String empleadoId, Empleado empleadoDetalle);

}
