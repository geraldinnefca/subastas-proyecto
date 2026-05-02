package cl.sda1085.subastas.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "subastas")

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Subasta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long idProducto;  //Relacionado con microservicio 'productos'

    @Column(nullable = false)
    private Long idVendedor;  //Relacionado con microservicio 'usuarios' (vendedor)

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precioBase;

    @Column(nullable = false)
    private LocalDateTime fechaInicio;

    @Column(nullable = false)
    private LocalDateTime fechaTermino;

    @Column(nullable = false)
    private String estado;  //Programado, abierto, cerrado o cancelado

    private Long idGanador;  //Al final, guardará quién ganó para que el microservicio 'pagos' lo use
}
