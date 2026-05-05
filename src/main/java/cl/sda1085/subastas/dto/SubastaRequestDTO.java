package cl.sda1085.subastas.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class SubastaRequestDTO {

    //DTO de entrada
    //No es necesario el ID, se genera automáticamente

    @NotNull(message = "El ID del producto es obligatorio.")
    private Long idProducto;

    @NotNull(message = "El ID del vendedor es obligatorio.")
    private Long idVendedor;

    @NotNull(message = "El precio base es obligatorio.")
    @DecimalMin(value = "0.01", message = "El precio base debe ser mayor a cero.")
    private BigDecimal precioBase;

    @NotNull(message = "La fecha de inicio es obligatoria.")
    @FutureOrPresent(message = "La fecha de inicio debe ser hoy o una fecha futura.")
    private LocalDateTime fechaInicio;

    @NotNull(message = "La fecha de término es obligatoria.")
    @Future(message = "La fecha de término debe ser una fecha futura.")
    private LocalDateTime fechaTermino;

    @NotBlank(message = "El estado no debe estar vacío.")
    private String estado;
}
