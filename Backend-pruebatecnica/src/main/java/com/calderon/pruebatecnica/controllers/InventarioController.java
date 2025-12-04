package com.calderon.pruebatecnica.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.calderon.pruebatecnica.dto.ApiResponse;
import com.calderon.pruebatecnica.dto.Meta;
import com.calderon.pruebatecnica.dto.MensajeDTO;
import com.calderon.pruebatecnica.models.Inventario;
import com.calderon.pruebatecnica.service.InventarioService;
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
@RequestMapping("/api/inventarios")
public class InventarioController {
    private static final Logger logger = LoggerFactory.getLogger(InventarioController.class);

    @Autowired
    private InventarioService inventarioService;

    @PostMapping
    public ResponseEntity<ApiResponse<Object>> create(@RequestBody Inventario inventario) {
        try {
            Inventario saved = inventarioService.save(inventario);
            ApiResponse<Object> resp = new ApiResponse<>(new Meta("OK"), Map.of("Inventario", saved,
                    "Mensaje", new MensajeDTO("Se creó correctamente el Inventario ## " + saved.getSku())));

            return ResponseEntity.status(HttpStatus.CREATED).body(resp);
        } catch (Exception e) {
            logger.error("Error creando inventario", e);
            String mensaje = "Error creando inventario: " + (e.getMessage() != null ? e.getMessage() : e.toString());
            ApiResponse<Object> resp = new ApiResponse<>(new Meta("ERROR"), Map.of("Mensaje", new MensajeDTO(mensaje)));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> readOne(@PathVariable(value = "id") String id) {
        try {
            Inventario inventario = inventarioService.findById(id);
            ApiResponse<Object> resp = new ApiResponse<>(new Meta("OK"), Map.of("Inventario", inventario));
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            logger.error("Error leyendo inventario {}", id, e);
            String mensaje = "Error leyendo inventario: " + (e.getMessage() != null ? e.getMessage() : e.toString());
            ApiResponse<Object> resp = new ApiResponse<>(new Meta("ERROR"), Map.of("Mensaje", new MensajeDTO(mensaje)));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> update(@RequestBody Inventario inventarioDetails,
            @PathVariable(value = "id") String id) {
        try {
            Inventario inventario = inventarioService.update(id, inventarioDetails);
            ApiResponse<Object> resp = new ApiResponse<>(new Meta("OK"),
                    Map.of("Mensaje",
                            new MensajeDTO("Se actualizó correctamente el inventario ## " + inventario.getSku())));

            return ResponseEntity.status(HttpStatus.OK).body(resp);
        } catch (Exception e) {
            logger.error("Error actualizando inventario {}", id, e);
            String mensaje = "Error actualizando inventario: "
                    + (e.getMessage() != null ? e.getMessage() : e.toString());
            ApiResponse<Object> resp = new ApiResponse<>(new Meta("ERROR"), Map.of("Mensaje", new MensajeDTO(mensaje)));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> delete(@PathVariable(value = "id") String id) {
        try {
            inventarioService.deleteById(id);
            ApiResponse<Object> resp = new ApiResponse<>(new Meta("OK"),
                    Map.of("Mensaje", new MensajeDTO("Se eliminó correctamente el inventario ## " + id)));

            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            logger.error("Error eliminando inventario {}", id, e);
            String mensaje = "Error eliminando inventario: " + (e.getMessage() != null ? e.getMessage() : e.toString());
            ApiResponse<Object> resp = new ApiResponse<>(new Meta("ERROR"), Map.of("Mensaje", new MensajeDTO(mensaje)));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Object>> readAll() {
        try {
            List<Inventario> inventarios = inventarioService.findAll();
            ApiResponse<Object> resp = new ApiResponse<>(new Meta("OK"), Map.of("Inventarios", inventarios));
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            logger.error("Error listando inventarios", e);
            String mensaje = "Error listando inventarios: " + (e.getMessage() != null ? e.getMessage() : e.toString());
            ApiResponse<Object> resp = new ApiResponse<>(new Meta("ERROR"), Map.of("Mensaje", new MensajeDTO(mensaje)));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
        }
    }

}
