package com.calderon.pruebatecnica.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.calderon.pruebatecnica.models.Empleado;
import com.calderon.pruebatecnica.models.Inventario;
import com.calderon.pruebatecnica.models.Poliza;
import com.calderon.pruebatecnica.repository.EmpleadoRepository;
import com.calderon.pruebatecnica.repository.InventarioRepository;
import com.calderon.pruebatecnica.repository.PolizasRepository;
import com.calderon.pruebatecnica.service.PolizasService;

@Service
public class PolizasServiceImpl implements PolizasService {
    private static final Logger logger = LoggerFactory.getLogger(PolizasServiceImpl.class);

    @Autowired
    private PolizasRepository polizasRepository;

    @Autowired
    private InventarioRepository inventarioRepository;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Override
    public List<Poliza> findAll() {
        return polizasRepository.findAll();
    }

    @Override
    public Optional<Poliza> findById(String id) {
        return polizasRepository.findById(id);
    }

    @Override
    @Transactional
    public Poliza update(String polizaId, Poliza polizaDetalle) {
        if (polizaDetalle == null) {
            throw new IllegalArgumentException("Datos a actualizar vacios");
        }

        Optional<Poliza> polizaOpt = polizasRepository.findById(polizaId);

        if (!polizaOpt.isPresent()) {
            throw new IllegalArgumentException("Poliza no encontrada");
        }

        Poliza polizaVieja = polizaOpt.get();

        if (polizaDetalle.getEmpleadoGenero() != null) {
            Optional<Empleado> empleadoOpt = empleadoRepository.findById(polizaDetalle.getEmpleadoGenero());

            if (!empleadoOpt.isPresent()) {
                throw new IllegalArgumentException("Empleado no encontrado");
            }

            polizaVieja.setEmpleado(empleadoOpt.get());
        }

        if (polizaDetalle.getSku() != null) {
            Integer cantidad = polizaDetalle.getCantidad();

            if (cantidad == null) {
                throw new IllegalArgumentException("Cantidad no encontrada");
            }

            if (cantidad <= 0) {
                throw new IllegalArgumentException("Cantidad negativa o cero");
            }

            Optional<Inventario> inventarioOpt = inventarioRepository.findById(polizaDetalle.getSku());

            if (!inventarioOpt.isPresent()) {
                throw new IllegalArgumentException("Inventario no encontrado");
            }

            Inventario inventario = inventarioOpt.get();
            Inventario inventarioViejo = polizaVieja.getInventario();

            if (inventario.getSku() != inventarioViejo.getSku()) {
                inventarioViejo.setCantidad(inventarioViejo.getCantidad() + polizaVieja.getCantidad());

                if (inventario.getCantidad() < cantidad) {
                    throw new IllegalArgumentException("No hay suficiente inventario");
                }

                inventario.setCantidad(inventario.getCantidad() - cantidad);

                inventarioRepository.save(inventarioViejo);
                polizaVieja.setInventario(inventario);
            } else {
                Integer diferenciaPolizas = polizaDetalle.getCantidad() - polizaVieja.getCantidad();

                if (inventario.getCantidad() < diferenciaPolizas) {
                    throw new IllegalArgumentException("No hay suficiente inventario");
                }

                inventario.setCantidad(inventario.getCantidad() - diferenciaPolizas);
            }

            polizaVieja.setCantidad(polizaDetalle.getCantidad());

            inventarioRepository.save(inventario);
        }

        if (polizaDetalle.getFecha() != null) {
            polizaVieja.setFecha(polizaDetalle.getFecha());
        }

        return polizasRepository.save(polizaVieja);
    }

    @Override
    @Transactional
    public Poliza save(Poliza poliza) {
        if (poliza == null) {
            throw new IllegalArgumentException("Poliza no puede ser null");
        }

        // poliza.setFecha(LocalDateTime.now());

        if (poliza.getFecha() == null) {
            throw new IllegalArgumentException("Fecha invalida");
        }

        if (poliza.getSku() == null) {
            throw new IllegalArgumentException("Falta el valor del SKU");
        }

        Optional<Inventario> inventarioOpt = inventarioRepository.findById(poliza.getSku());

        if (!inventarioOpt.isPresent()) {
            throw new IllegalArgumentException("Inventario no encontrado");
        }

        Inventario inventario = inventarioOpt.get();

        poliza.setInventario(inventario);

        if (inventario.getCantidad() < poliza.getCantidad()) {
            throw new IllegalArgumentException("Inventario no cuenta con la cantidad requerida");
        }

        inventario.setCantidad(inventario.getCantidad() - poliza.getCantidad());

        inventarioRepository.save(inventario);

        if (poliza.getCantidad() == null) {
            throw new IllegalArgumentException("Falta valor de cantidad");
        }

        if (poliza.getCantidad() <= 0) {
            throw new IllegalArgumentException("Cantidad menor o igual a cero");
        }

        if (poliza.getEmpleadoGenero() == null) {
            throw new IllegalArgumentException("Falta el valor del empleado");
        }

        Optional<Empleado> empleadoOpt = empleadoRepository.findById(poliza.getEmpleadoGenero());

        if (!empleadoOpt.isPresent()) {
            throw new IllegalArgumentException("Empleado no encontrado");
        }

        poliza.setEmpleado(empleadoOpt.get());

        return polizasRepository.save(poliza);
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        Optional<Poliza> polizaOpt = polizasRepository.findById(id);

        if (!polizaOpt.isPresent()) {
            throw new IllegalArgumentException("Poliza no encontrada");
        }

        Poliza poliza = polizaOpt.get();
        Inventario inventario = poliza.getInventario();

        inventario.setCantidad(inventario.getCantidad() + poliza.getCantidad());

        inventarioRepository.save(inventario);
        polizasRepository.deleteById(id);
    }

}
