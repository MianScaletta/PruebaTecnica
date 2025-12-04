package com.calderon.pruebatecnica.service;

import com.calderon.pruebatecnica.models.Poliza;

import java.util.List;
import java.util.Optional;

public interface PolizasService {

    public List<Poliza> findAll();

    public Optional<Poliza> findById(String id);

    public Poliza save(Poliza polizas);

    public void deleteById(String id);

    public Poliza update(String polizaId, Poliza poliza);
}
