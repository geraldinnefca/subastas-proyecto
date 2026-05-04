package cl.sda1085.subastas.service;

import cl.sda1085.subastas.dto.SubastaResponseDTO;
import cl.sda1085.subastas.model.Subasta;
import cl.sda1085.subastas.repository.SubastaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubastaService {

    private final SubastaRepository subastaRepository;

    //Método de apoyo para encapsulamiento de datos
    private SubastaResponseDTO mapToResponseDTO(Subasta subasta){
        return new SubastaResponseDTO(
                subasta.getId(),
                sub
        );
    }
}
