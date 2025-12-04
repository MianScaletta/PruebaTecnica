package com.calderon.pruebatecnica.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.calderon.pruebatecnica.models.Inventario;
import com.calderon.pruebatecnica.repository.InventarioRepository;
import com.calderon.pruebatecnica.service.InventarioService;

@Service
public class InventarioServiceImpl implements InventarioService {

    @Autowired
    private InventarioRepository inventarioRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Inventario> findAll() {
        return inventarioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Inventario findById(String id) {
        Optional<Inventario> inventarioOpt = inventarioRepository.findById(id);

        if (!inventarioOpt.isPresent()) {
            throw new IllegalArgumentException("Inventario no encontrado");
        }

        return inventarioOpt.get();
    }

    @Override
    @Transactional
    public Inventario save(Inventario inventario) {
        if (inventario == null) {
            throw new IllegalArgumentException("Inventario no puede ser null");
        }

        if (inventario.getCantidad() == null) {
            throw new IllegalArgumentException("Cantidad no puede ser null");
        }

        if (inventario.getCantidad() <= 0) {
            throw new IllegalArgumentException("Cantidad no puede ser cero o negativo");
        }

        if (inventario.getNombre() == null) {
            throw new IllegalArgumentException("Nombre no puede ser null");
        }

        return inventarioRepository.save(inventario);
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        Optional<Inventario> inventarioOpt = inventarioRepository.findById(id);

        if (!inventarioOpt.isPresent()) {
            throw new IllegalArgumentException("Inventario no encontrado");
        }

        Inventario inventario = inventarioOpt.get();

        if (!inventario.getPolizas().isEmpty()) {
            throw new IllegalArgumentException(
                    "Ya hay polizas asociadas con el inventario, favor de borrarlas antes de eliminar el inventario");
        }

        inventarioRepository.deleteById(id);
    }

    @Override
    public Inventario update(String inventarioId, Inventario inventarioDetalle) {
        if (inventarioDetalle == null) {
            throw new IllegalArgumentException("Inventario no puede ser null");
        }

        Optional<Inventario> inventarioOpt = inventarioRepository.findById(inventarioId);

        if (!inventarioOpt.isPresent()) {
            throw new IllegalArgumentException("Inventario no encontrado");
        }

        Inventario inventario = inventarioOpt.get();

        if (inventarioDetalle.getCantidad() != null) {
            if (inventarioDetalle.getCantidad() < 0) {
                throw new IllegalArgumentException("Cantidad negativa o cero");
            }

            inventario.setCantidad(inventarioDetalle.getCantidad());
        }

        if (inventarioDetalle.getNombre() != null) {
            inventario.setNombre(inventarioDetalle.getNombre());
        }

        return inventarioRepository.save(inventario);
    }

}
