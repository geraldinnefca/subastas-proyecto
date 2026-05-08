package cl.sda1085.subastas.service;

import cl.sda1085.subastas.dto.SubastaRequestDTO;
import cl.sda1085.subastas.dto.SubastaResponseDTO;
import cl.sda1085.subastas.model.Subasta;
import cl.sda1085.subastas.repository.SubastaRepository;
import cl.sda1085.subastas.webclient.ProductoClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubastaService {

    private final SubastaRepository subastaRepository;
    private final ProductoClient productoClient;

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

    //Método que convierte 'DTO' en una entidad
    private Subasta mapToEntity(SubastaRequestDTO dto) {
        Subasta subasta = new Subasta();
        subasta.setIdProducto(dto.getIdProducto());
        subasta.setIdVendedor(dto.getIdVendedor());
        subasta.setPrecioBase(dto.getPrecioBase());
        subasta.setFechaInicio(dto.getFechaInicio());
        subasta.setFechaTermino(dto.getFechaTermino());
        subasta.setEstado("PROGRAMADA");

        return subasta;
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
            subastaExistente.setIdProducto(dto.getIdProducto());
            subastaExistente.setIdVendedor(dto.getIdVendedor());
            subastaExistente.setPrecioBase(dto.getPrecioBase());
            subastaExistente.setFechaInicio(dto.getFechaInicio());
            subastaExistente.setFechaTermino(dto.getFechaTermino());
            subastaExistente.setEstado(dto.getEstado());

            Subasta subastaActualizada = subastaRepository.save(subastaExistente);
            return mapToResponseDTO(subastaActualizada);
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


    //Tarea programada (ejecución cada un minuto) que busca subastas abiertas cuya fecha de término ya pasó y las cierra
    @Scheduled(fixedRate = 60000) //Scheduled indica que el metodo debe ejecutarse automaticamente contando desde el inicio de la última ejecución, fixedRate es el tiempo en milisegundos
    public void cerrarSubastasVencidas(){
        LocalDateTime fechaInicio = LocalDateTime.now();
        log.info("Ejecutando revisión de subastas vencidas a las {}", fechaInicio);

        //Buscar las subastas que deberían haber terminado
        List<Subasta> vencidas = subastaRepository.findByFechaTerminoBefore(fechaInicio);
        for (Subasta subasta : vencidas) {
            //Sólo cierra las que aún figuran como estado abiertas o programadas
            if (!subasta.getEstado().equals("CERRADA")) {
                subasta.setEstado("CERRADA");
                subastaRepository.save(subasta);
                log.info("Subasta ID {} marcada como CERRADA por vencimiento", subasta.getId());
            }
        }
    }

    //Registra subasta a partir de WebClient
    public SubastaResponseDTO registrarSubasta(SubastaRequestDTO dto){

        if (dto.getFechaInicio().isAfter(dto.getFechaTermino())){  //Validación de fechas
            throw new RuntimeException("La fecha de inicio no puede ser posterior a la fecha de término.");
        }

        productoClient.obtenerProductoPorId(dto.getIdProducto());  //Validación de producto (WebClient)
        Subasta subasta = mapToEntity(dto);  //Mapeo y persistencia

        if (subasta.getEstado() == null){
            subasta.setEstado("PROGRAMADA");  //Asegurar el estado por defecto
        }

        Subasta subastaGuardada = subastaRepository.save(subasta);  //Guardar y responder
        return mapToResponseDTO(subastaGuardada);
    }
}
