package cl.sda1085.subastas.service;

import cl.sda1085.subastas.dto.SubastaRequestDTO;
import cl.sda1085.subastas.dto.SubastaResponseDTO;
import cl.sda1085.subastas.model.Subasta;
import cl.sda1085.subastas.repository.SubastaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubastaService {

    private final SubastaRepository subastaRepository;

    //Método de apoyo para encapsulamiento de datos
    private SubastaResponseDTO mapToResponseDTO(Subasta subasta){
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

    //Obtener todas las subastas
    public List<SubastaResponseDTO> obtenerTodas(){
        return subastaRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    //Obtener por ID
    public Optional<SubastaResponseDTO> obtenerPorId(Long id){
        return subastaRepository.findById(id)
                .map(this::mapToResponseDTO);
    }

    //Crear (guardar) con validación de fechas
    public SubastaResponseDTO guardar(SubastaRequestDTO dto){

        if (dto.getFechaInicio().isAfter(dto.getFechaTermino())){
            throw new RuntimeException("La fecha de inicio no puede ser posterior a la de término.");
        }

        Subasta subasta = new Subasta();
        subasta.setIdProducto(dto.getIdProducto());
        subasta.setIdVendedor(dto.getIdVendedor());
        subasta.setPrecioBase(dto.getPrecioBase());
        subasta.setFechaInicio(dto.getFechaInicio());
        subasta.setFechaTermino(dto.getFechaTermino());

        //Estado inicial por defecto
        subasta.setEstado("PROGRAMADA");

        //Devolver la respuesta como DTO
        return mapToResponseDTO(subastaRepository.save(subasta));
    }

    //Actualizar subasta
    public Optional<SubastaResponseDTO> actualizar(Long id, SubastaRequestDTO dto){
        return subastaRepository.findById(id).map(subastaExistente -> {
            subastaExistente.setPrecioBase(dto.getPrecioBase());
            subastaExistente.setFechaInicio(dto.getFechaInicio());
            subastaExistente.setFechaTermino(dto.getFechaTermino());

            //El estado podría cambiar aquí si se cancela o se abre manualmente
            return mapToResponseDTO(subastaRepository.save(subastaExistente));
        });
    }

    //Eliminar subasta
    public void eliminar(Long id){
        subastaRepository.deleteById(id);
    }


    //CRUD personalizado

    //Busca subastas por estado
    public List<SubastaResponseDTO> obtenerPorEstado(String estado){
        return subastaRepository.findByEstado(estado).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    //Busca subastas de un producto específico
    public List<SubastaResponseDTO> obtenerPorIdProducto(Long idProducto){
        return subastaRepository.findByIdProducto(idProducto).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    //Busca subastas que finalizan antes de una fecha
    public List<SubastaResponseDTO> obtenerSubastasPorVencer(LocalDateTime fecha){
        return subastaRepository.findByFechaTerminoBefore(fecha).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    //Busca la subasta activa de un producto
    public Optional<SubastaResponseDTO> obtenerSubastaActivaProducto(Long idProducto){
        return subastaRepository.findByIdProductoAndEstado(idProducto, "ABIERTA")
                .map(this::mapToResponseDTO);
    }

    //Verifica si un vendedor ya tiene una subasta activa
    public boolean vendedorTieneSubastaActiva(Long idVendedor){
        return subastaRepository.existsByIdVendedorAndEstado(idVendedor, "ABIERTA");
    }

    //Encuentra la subasta que terminará más pronto
    public Optional<SubastaResponseDTO> obtenerSubastaMasUrgente(){
        return subastaRepository.findTopByEstadoOrderByFechaTerminoAsc("ABIERTA")
                .map(this::mapToResponseDTO);
    }

    //Verifica si un producto ya está registrado en el sistema
    public boolean productoYaTieneSubasta(Long idProducto){
        return subastaRepository.existsByIdProducto(idProducto);
    }
}
