package com.calderon.pruebatecnica.service;

import com.calderon.pruebatecnica.models.Empleado;

import java.util.List;

public interface EmpleadoService {

    public List<Empleado> findAll();

    public Empleado findById(String id);

    public Empleado save(Empleado empleado);

    public void deleteById(String id);

    public Empleado update(String empleadoId, Empleado empleadoDetalle);

}
