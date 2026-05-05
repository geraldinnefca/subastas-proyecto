package cl.sda1085.subastas.controller;

import cl.sda1085.subastas.dto.SubastaRequestDTO;
import cl.sda1085.subastas.dto.SubastaResponseDTO;
import cl.sda1085.subastas.service.SubastaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/subastas")

public class SubastaController {

    private final SubastaService subastaService;

    @GetMapping
    public ResponseEntity<List<SubastaResponseDTO>> obtenerTodas() {
        return ResponseEntity.ok(subastaService.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubastaResponseDTO> obtenerPorId(@PathVariable Long id) {
        return subastaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SubastaResponseDTO> crear(@Valid @RequestBody SubastaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(subastaService.guardar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubastaResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody SubastaRequestDTO dto) {

        return subastaService.actualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/producto/{idProducto}")
    public ResponseEntity<List<SubastaResponseDTO>> obtenerPorProducto(@PathVariable Long idProducto) {
        return ResponseEntity.ok(subastaService.obtenerPorIdProducto(idProducto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        subastaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
