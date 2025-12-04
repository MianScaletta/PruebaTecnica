package com.calderon.pruebatecnica.service;

import org.springframework.data.domain.Page;

import com.calderon.pruebatecnica.models.Inventario;

import java.util.List;
import java.util.Optional;

public interface InventarioService {

    public List<Inventario> findAll();

    public Inventario findById(String id);

    public Inventario save(Inventario inventario);

    public void deleteById(String id);

    public Inventario update(String inventarioId, Inventario inventarioDetalle);

}
