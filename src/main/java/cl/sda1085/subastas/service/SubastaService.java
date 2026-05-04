package cl.sda1085.subastas.service;

import cl.sda1085.subastas.dto.SubastaRequestDTO;
import cl.sda1085.subastas.dto.SubastaResponseDTO;
import cl.sda1085.subastas.model.Subasta;
import cl.sda1085.subastas.repository.SubastaRepository;
import jakarta.persistence.metamodel.SingularAttribute;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class SubastaService {

    //Vínculo con repository
    private final SubastaRepository subastaRepository;

    private SubastaResponseDTO mapToDTO (Subasta subasta) {
        return new SubastaResponseDTO(
                subasta.getId(),
                subasta.getIdProducto(),
                subasta.getIdVendedor(),
                subasta.getPrecioBase(),
                subasta.getFechaInicio(),
                subasta.getFechaTermino(),
                subasta.getEstado(),
                subasta.getIdGanador()

        );
    }

    private SubastaResponseDTO convertirADTO (Subasta subasta){
        return new SubastaResponseDTO(
                subasta.getId(),
                subasta.getIdProducto(),
                subasta.getIdVendedor(),
                subasta.getPrecioBase(),
                subasta.getFechaInicio(),
                subasta.getFechaTermino(),
                subasta.getEstado(),
                subasta.getIdGanador()

        );
    }

    //Obtener todos
    public List<SubastaResponseDTO> obtenerTodos() {
        return subastaRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    //Obtener por ID
    public Optional<SubastaResponseDTO> obtenerPorId(Long id){  //'Optional' permite contener un objeto que puede o no ser nulo
        return subastaRepository.findById(id).map(this::mapToDTO);
    }

    //Guardar
    public SubastaResponseDTO guardar(SubastaRequestDTO dto){
        Subasta subasta = new Subasta(
                null,
                dto.getIdProducto(),
                dto.getIdVendedor(),
                dto.getPrecioBase(),
                dto.getFechaInicio(),
                dto.getFechaTermino(),
                dto.getEstado()
        );
        return mapToDTO(subastaRepository.save(subasta));
    }
    //Actualizar
    public Optional<SubastaResponseDTO> actualizar(Long id, SubastaRequestDTO dto){
        return subastaRepository.findById(id).map(disponible -> {
            Subasta subasta = subastaRepository
                    .findById(dto.getIdProducto())
                    .orElseThrow(() -> new RuntimeException(
                            "Categoría NO encontrada por ID: " + dto.getIdProducto()));

            disponible.setIdProducto(dto.getIdProducto()),
            disponible.setIdVendedor(dto.getIdVendedor()),
            disponible.setPrecioBase(dto.getPrecioBase()),
            disponible.setFechaInicio(dto.getFechaInicio()),
            disponible.setFechaTermino(dto.getFechaTermino()),
            disponible.setEstado(dto.getEstado());

            return mapToDTO(subastaRepository.save(disponible));
        });
    }

    //Eliminar
    public void eliminar(Long id){
        subastaRepository.deleteById(id);
    }
}


}
