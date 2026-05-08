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
        if (subastaRepository.count() > 0) {
            log.info("Base de datos de subastas ya contiene datos. Omitiendo inicialización");
            return;
        }
        log.info("Generando 10 subastas de prueba vinculadas a los vendedores 1, 2 y 3...");

        //Subastas ABIERTAS (listas para recibir pujas)
        Subasta s1 = new Subasta(null, 1L, 1L, new BigDecimal("150000.00"), LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(3), "ABIERTA", null);
        Subasta s2 = new Subasta(null, 2L, 2L, new BigDecimal("300000.00"), LocalDateTime.now().minusHours(5), LocalDateTime.now().plusHours(20), "ABIERTA", null);
        Subasta s3 = new Subasta(null, 3L, 3L, new BigDecimal("500000.00"), LocalDateTime.now().minusMinutes(30), LocalDateTime.now().plusDays(1), "ABIERTA", null);
        Subasta s4 = new Subasta(null, 6L, 1L, new BigDecimal("220000.00"), LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(2), "ABIERTA", null);

        //Subastas PROGRAMADAS (iniciarán en el futuro)
        Subasta s5 = new Subasta(null, 7L, 2L, new BigDecimal("400000.00"), LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(5), "PROGRAMADA", null);
        Subasta s6 = new Subasta(null, 8L, 3L, new BigDecimal("350000.00"), LocalDateTime.now().plusHours(12), LocalDateTime.now().plusDays(3), "PROGRAMADA", null);
        Subasta s7 = new Subasta(null, 10L, 1L, new BigDecimal("180000.00"), LocalDateTime.now().plusMinutes(10), LocalDateTime.now().plusHours(5), "PROGRAMADA", null);

        //Subastas CERRADAS (ya terminaron)
        Subasta s8 = new Subasta(null, 12L, 2L, new BigDecimal("270000.00"), LocalDateTime.now().minusDays(10), LocalDateTime.now().minusDays(2), "CERRADA", 4L);
        Subasta s9 = new Subasta(null, 15L, 3L, new BigDecimal("600000.00"), LocalDateTime.now().minusDays(5), LocalDateTime.now().minusMinutes(1), "CERRADA", 5L);
        Subasta s10 = new Subasta(null, 18L, 2L, new BigDecimal("210000.00"), LocalDateTime.now().minusDays(7), LocalDateTime.now().minusDays(1), "CERRADA", 6L);

        subastaRepository.saveAll(List.of(s1, s2, s3, s4, s5, s6, s7, s8, s9, s10));
        log.info("¡Subastas de prueba creadas con éxito: 4 Abiertas, 3 Programadas y 3 Cerradas.");
    }
}
