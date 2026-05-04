package cl.sda1085.subastas.controller;

import cl.sda1085.subastas.dto.SubastaRequestDTO;
import cl.sda1085.subastas.dto.SubastaResponseDTO;
import cl.sda1085.subastas.service.SubastaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RestController
@RequiredArgsConstructor

public class SubastaController {

    private final SubastaService subastaService;

    //obtener
    @GetMapping
    public ResponseEntity<SubastaResponseDTO> obtenerTodos() {
        return subastaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }

    //guardar
    @PostMapping
    public ResponseEntity<SubastaResponseDTO> guardar(@Valid @RequestBody SubastaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(subastaService.guardar(dto));
    }

    //actualizar
    @PutMapping
    public ResponseEntity<SubastaResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody SubastaRequestDTO dto) {

        return subastaService.actualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }

    //eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        subastaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}


