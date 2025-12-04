package com.calderon.pruebatecnica.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.calderon.pruebatecnica.dto.ApiResponse;
import com.calderon.pruebatecnica.dto.Meta;
import com.calderon.pruebatecnica.dto.MensajeDTO;
import com.calderon.pruebatecnica.models.Empleado;
import com.calderon.pruebatecnica.service.EmpleadoService;
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
@RequestMapping("/api/empleados")
public class EmpleadoController {
    private static final Logger logger = LoggerFactory.getLogger(EmpleadoController.class);

    @Autowired
    private EmpleadoService empleadoService;

    @PostMapping
    public ResponseEntity<ApiResponse<Object>> createUser(@RequestBody Empleado empleado) {
        try {
            Empleado saved = empleadoService.save(empleado);
            ApiResponse<Object> resp = new ApiResponse<>(new Meta("OK"),
                    Map.of("Empleado", saved,
                            "Mensaje",
                            new MensajeDTO("Se creó correctamente el empleado ## " + saved.getIdEmpleado())));

            return ResponseEntity.status(HttpStatus.CREATED).body(resp);
        } catch (Exception e) {
            logger.error("Error creando empleado", e);
            String mensaje = "Error creando empleado: " + (e.getMessage() != null ? e.getMessage() : e.toString());
            ApiResponse<Object> resp = new ApiResponse<>(new Meta("ERROR"), Map.of("Mensaje", new MensajeDTO(mensaje)));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> readOne(@PathVariable(value = "id") String id) {
        try {
            Empleado empleado = empleadoService.findById(id);
            ApiResponse<Object> resp = new ApiResponse<>(new Meta("OK"), Map.of("Empleado", empleado));
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            logger.error("Error leyendo empleado {}", id, e);
            String mensaje = "Error leyendo empleado: " + (e.getMessage() != null ? e.getMessage() : e.toString());
            ApiResponse<Object> resp = new ApiResponse<>(new Meta("ERROR"), Map.of("Mensaje", new MensajeDTO(mensaje)));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> update(@RequestBody Empleado empleadoDetails,
            @PathVariable(value = "id") String id) {
        try {
            Empleado saved = empleadoService.update(id, empleadoDetails);
            ApiResponse<Object> resp = new ApiResponse<>(new Meta("OK"), Map.of("Mensaje",
                    new MensajeDTO("Se actualizó correctamente el empleado ## " + saved.getIdEmpleado())));

            return ResponseEntity.status(HttpStatus.OK).body(resp);
        } catch (Exception e) {
            logger.error("Error actualizando empleado {}", id, e);
            String mensaje = "Error actualizando empleado: " + (e.getMessage() != null ? e.getMessage() : e.toString());
            ApiResponse<Object> resp = new ApiResponse<>(new Meta("ERROR"), Map.of("Mensaje", new MensajeDTO(mensaje)));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> delete(@PathVariable(value = "id") String id) {
        try {
            empleadoService.deleteById(id);
            ApiResponse<Object> resp = new ApiResponse<>(new Meta("OK"),
                    Map.of("Mensaje", new MensajeDTO("Se eliminó correctamente el empleado ## " + id)));

            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            logger.error("Error eliminando empleado {}", id, e);
            String mensaje = "Error eliminando empleado: " + (e.getMessage() != null ? e.getMessage() : e.toString());
            ApiResponse<Object> resp = new ApiResponse<>(new Meta("ERROR"), Map.of("Mensaje", new MensajeDTO(mensaje)));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Object>> readAll() {
        try {
            List<Empleado> empleados = empleadoService.findAll();
            ApiResponse<Object> resp = new ApiResponse<>(new Meta("OK"), Map.of("Empleados", empleados));
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            logger.error("Error listando empleados", e);
            String mensaje = "Error listando empleados: " + (e.getMessage() != null ? e.getMessage() : e.toString());
            ApiResponse<Object> resp = new ApiResponse<>(new Meta("ERROR"), Map.of("Mensaje", new MensajeDTO(mensaje)));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
        }
    }
}
