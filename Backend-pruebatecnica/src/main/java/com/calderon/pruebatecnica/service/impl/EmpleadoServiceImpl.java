package com.calderon.pruebatecnica.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.calderon.pruebatecnica.models.Empleado;
import com.calderon.pruebatecnica.repository.EmpleadoRepository;
import com.calderon.pruebatecnica.service.EmpleadoService;

@Service
public class EmpleadoServiceImpl implements EmpleadoService {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Empleado> findAll() {
        return empleadoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Empleado findById(String id) {
        Optional<Empleado> empleadoOpt = empleadoRepository.findById(id);

        if (!empleadoOpt.isPresent()) {
            throw new IllegalArgumentException("Empleado no encontrado");
        }

        return empleadoOpt.get();
    }

    @Override
    @Transactional
    public Empleado save(Empleado empleado) {
        if (empleado == null) {
            throw new IllegalArgumentException("Empleado no puede ser null");
        }

        if (empleado.getNombre() == null) {
            throw new IllegalArgumentException("Nombre no puede ser null");
        }

        if (empleado.getApellido() == null) {
            throw new IllegalArgumentException("Apellido no puede ser null");
        }

        if (empleado.getPuesto() == null) {
            throw new IllegalArgumentException("Puesto no puede ser null");
        }

        return empleadoRepository.save(empleado);
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        Optional<Empleado> empleadoOpt = empleadoRepository.findById(id);

        if (!empleadoOpt.isPresent()) {
            throw new IllegalArgumentException("Empleado no encontrado");
        }

        Empleado empleado = empleadoOpt.get();

        if (!empleado.getPolizas().isEmpty()) {
            throw new IllegalArgumentException(
                    "Ya hay polizas asociadas con el empleado, favor de borrarlas antes de eliminar el empleado");
        }

        empleadoRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Empleado update(String empleadoId, Empleado empleadoDetalle) {
        if (empleadoDetalle == null) {
            throw new IllegalArgumentException("Datos a actualizar vacios");
        }

        Optional<Empleado> empleadoOpt = empleadoRepository.findById(empleadoId);

        if (!empleadoOpt.isPresent()) {
            throw new IllegalArgumentException("Empleado no encontrado");
        }

        Empleado empleado = empleadoOpt.get();

        if (empleadoDetalle.getNombre() != null) {
            empleado.setNombre(empleadoDetalle.getNombre());
        }

        if (empleadoDetalle.getApellido() != null) {
            empleado.setApellido(empleadoDetalle.getApellido());
        }

        if (empleadoDetalle.getPuesto() != null) {
            empleado.setPuesto(empleadoDetalle.getPuesto());
        }

        return empleadoRepository.save(empleado);
    }

}
