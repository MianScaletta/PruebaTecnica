package com.calderon.pruebatecnica.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.calderon.pruebatecnica.models.Poliza;

@Repository
public interface PolizasRepository extends JpaRepository<Poliza, String> {

}
