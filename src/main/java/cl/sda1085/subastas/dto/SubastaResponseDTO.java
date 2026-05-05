package cl.sda1085.subastas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class SubastaResponseDTO {

    //DTO de salida (respuesta)
    //No existen las anotaciones de validación

    private Long id;  //ID generado en la base de datos
    private Long idProducto;
    private Long idVendedor;
    private BigDecimal precioBase;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaTermino;
    private String estado;
    private Long idGanador;  //Se llena al finalizar la subasta
}
