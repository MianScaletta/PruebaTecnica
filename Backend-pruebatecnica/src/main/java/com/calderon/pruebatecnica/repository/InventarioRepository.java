package com.calderon.pruebatecnica.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.calderon.pruebatecnica.models.Inventario;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, String> {

}
