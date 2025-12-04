package com.calderon.pruebatecnica.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.calderon.pruebatecnica.models.Empleado;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, String> {

}
