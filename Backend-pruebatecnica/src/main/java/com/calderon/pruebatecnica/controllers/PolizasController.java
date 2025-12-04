package com.calderon.pruebatecnica.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.calderon.pruebatecnica.dto.ApiResponse;
import com.calderon.pruebatecnica.dto.Meta;
import com.calderon.pruebatecnica.dto.MensajeDTO;
import com.calderon.pruebatecnica.models.Poliza;
import com.calderon.pruebatecnica.service.PolizasService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/polizas")
public class PolizasController {
    private static final Logger logger = LoggerFactory.getLogger(PolizasController.class);

    @Autowired
    private PolizasService polizasService;

    @PostMapping
    public ResponseEntity<ApiResponse<Object>> create(@RequestBody Poliza poliza) {
        try {
            Poliza saved = polizasService.save(poliza);
            ApiResponse<Object> resp = new ApiResponse<>(new Meta("OK"), Map.of("Poliza", saved,
                    "Mensaje", new MensajeDTO("Se creó correctamente la Poliza ## " + saved.getIdPolizas())));
            return ResponseEntity.status(HttpStatus.CREATED).body(resp);
        } catch (Exception e) {
            logger.error("Error creando poliza", e);
            String mensaje = "Error creando poliza: " + (e.getMessage() != null ? e.getMessage() : e.toString());
            ApiResponse<Object> resp = new ApiResponse<>(new Meta("ERROR"), Map.of("Mensaje", new MensajeDTO(mensaje)));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> readOne(@PathVariable(value = "id") String id) {
        try {
            Optional<Poliza> poliza = polizasService.findById(id);
            if (!poliza.isPresent()) {
                ApiResponse<Object> resp = new ApiResponse<>(new Meta("ERROR"),
                        Map.of("Mensaje", new MensajeDTO("No encontrado")));
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp);
            }
            ApiResponse<Object> resp = new ApiResponse<>(new Meta("OK"), Map.of("Poliza", poliza.get()));
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            logger.error("Error leyendo poliza {}", id, e);
            String mensaje = "Error leyendo poliza: " + (e.getMessage() != null ? e.getMessage() : e.toString());
            ApiResponse<Object> resp = new ApiResponse<>(new Meta("ERROR"), Map.of("Mensaje", new MensajeDTO(mensaje)));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody Poliza polizaDetails,
            @PathVariable(value = "id") String id) {
        try {
            Poliza poliza = polizasService.update(id, polizaDetails);
            ApiResponse<Object> resp = new ApiResponse<>(new Meta("OK"),
                    Map.of("Mensaje",
                            new MensajeDTO("Se actualizó correctamente la poliza ## " +
                                    poliza.getIdPolizas())));
            return ResponseEntity.status(HttpStatus.OK).body(resp);
        } catch (Exception e) {
            logger.error("Error actualizando poliza {}", id, e);
            String mensaje = "Error actualizando poliza: " + (e.getMessage() != null ? e.getMessage() : e.toString());
            ApiResponse<Object> resp = new ApiResponse<>(new Meta("ERROR"), Map.of("Mensaje", new MensajeDTO(mensaje)));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> delete(@PathVariable(value = "id") String id) {
        try {
            if (!polizasService.findById(id).isPresent()) {
                ApiResponse<Object> resp = new ApiResponse<>(new Meta("ERROR"),
                        Map.of("Mensaje", new MensajeDTO("No encontrado")));
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp);
            }

            polizasService.deleteById(id);
            ApiResponse<Object> resp = new ApiResponse<>(new Meta("OK"),
                    Map.of("Mensaje", new MensajeDTO("Se eliminó correctamente la póliza ## " + id)));
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            logger.error("Error eliminando poliza {}", id, e);
            String mensaje = "Error eliminando poliza: " + (e.getMessage() != null ? e.getMessage() : e.toString());
            ApiResponse<Object> resp = new ApiResponse<>(new Meta("ERROR"), Map.of("Mensaje", new MensajeDTO(mensaje)));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Object>> readAll() {
        try {
            List<Poliza> polizas = polizasService.findAll();
            ApiResponse<Object> resp = new ApiResponse<>(new Meta("OK"), Map.of("Polizas", polizas));
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            logger.error("Error listando polizas", e);
            String mensaje = "Error listando polizas: " + (e.getMessage() != null ? e.getMessage() : e.toString());
            ApiResponse<Object> resp = new ApiResponse<>(new Meta("ERROR"), Map.of("Mensaje", new MensajeDTO(mensaje)));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
        }
    }

}
