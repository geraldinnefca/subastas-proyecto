package cl.sda1085.subastas.config;

import cl.sda1085.subastas.model.Subasta;
import cl.sda1085.subastas.repository.SubastaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor

public class DataInitializer implements CommandLineRunner {

    private final SubastaRepository subastaRepository;

    @Override
    public void run (String... args) {

        //Verificar si existen productos para no duplicarlos
        if (subastaRepository.count()>0) {
            log.info("Base de datos de subastas ya contiene datos. Omitiendo inicialización");
        }

        log.info("Iniciando la creación de productos de prueba...");

        Subasta s1 = new Subasta(
                null,
                1L,
                100L,
                new BigDecimal("50000.00"),
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(5),
                "PROGRAMADA", null);

        Subasta s2 = new Subasta(
                null,
                2L,
                101L,
                new BigDecimal("120000.50"),
                LocalDateTime.now(), // Inicia ahora
                LocalDateTime.now().plusHours(12), // Termina en 12 horas
                "ABIERTA",
                null
        );

        subastaRepository.saveAll(List.of(s1,s2));
        log.info("¡Subastas de prueba creadas con éxito!");

    }
}
