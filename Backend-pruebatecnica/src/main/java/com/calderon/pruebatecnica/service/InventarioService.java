package com.calderon.pruebatecnica.service;

import com.calderon.pruebatecnica.models.Inventario;

import java.util.List;

public interface InventarioService {

    public List<Inventario> findAll();

    public Inventario findById(String id);

    public Inventario save(Inventario inventario);

    public void deleteById(String id);

    public Inventario update(String inventarioId, Inventario inventarioDetalle);

}
