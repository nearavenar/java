package org.aravena.test.springboot.app;

import org.aravena.test.springboot.app.model.Cuenta;
import org.aravena.test.springboot.app.repository.CuentaRepository;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

//Pruebas de integracion -> habilita todas las preexistencias y repositorios
//@Tag("integracion_jpa")
@DataJpaTest
public class IntegracionJpaTest {
    
    @Autowired
    CuentaRepository cuentaRepository;

    @Test
    void findByIdTest() {
        Optional<Cuenta> cuenta = cuentaRepository.findById(1L);
        assertTrue(cuenta.isPresent());
        assertEquals("Nicolás", cuenta.orElseThrow().getPersona());
        assertEquals("1000.00", cuenta.orElseThrow().getSaldo().toPlainString());
    }

    @Test
    void findByPersonaTest() {
        Optional<Cuenta> cuenta = cuentaRepository.findByPersona("Nicolás");
        assertTrue(cuenta.isPresent());
        assertEquals(1L, cuenta.orElseThrow().getId());
        assertEquals("1000.00", cuenta.orElseThrow().getSaldo().toPlainString());
    }

    @Test
    void findByPersonaThrowExceptionTest() {
        Optional<Cuenta> cuenta = cuentaRepository.findByPersona("José");
        assertThrows(NoSuchElementException.class, cuenta::orElseThrow);
        assertFalse(cuenta.isPresent());
    }

    @Test
    void findAllTest() {
        List<Cuenta> cuentas = cuentaRepository.findAll();
        assertFalse(cuentas.isEmpty());
        assertTrue(cuentas.stream().iterator().hasNext());
        assertEquals(2, cuentas.size());
    }

    @Test
    void saveTest() {
        //Given -> una cuenta dada
        Cuenta cuenta = new Cuenta(null, "José", new BigDecimal("3000"));

        //When -> cuando se persiste
        Cuenta cuentaSave = cuentaRepository.save(cuenta);
        //Cuenta nuevaCuenta = cuentaRepository.findByPersona("José").orElseThrow();
        //Cuenta nuevaCuenta = cuentaRepository.findById(cuentaSave.getId()).orElseThrow();

        //Then -> entocnes se prueba
        assertNotNull(cuentaSave);
        assertEquals("José", cuentaSave.getPersona());
        assertEquals("3000", cuentaSave.getSaldo().toPlainString());
        //assertEquals(3, nuevaCuenta.getId());
    }

    @Test
    void updateTest() {
        //Given -> una cuenta dada
        Cuenta cuentaJose = new Cuenta(null, "José", new BigDecimal("3000"));

        //When -> cuando se persiste
        Cuenta cuenta = cuentaRepository.save(cuentaJose);

        //Then -> entocnes se prueba
        assertNotNull(cuenta);
        assertEquals("José", cuenta.getPersona());
        assertEquals("3000", cuenta.getSaldo().toPlainString());

        //When
        cuenta.setSaldo(new BigDecimal("3800"));
        cuenta.setPersona("Octavio");
        Cuenta cuentaUpd = cuentaRepository.save(cuenta);

        //Then
        assertEquals("Octavio", cuentaUpd.getPersona());
        assertEquals("3800", cuentaUpd.getSaldo().toPlainString());
    }

    @Test
    void deleteTest() {
        //Given
        Cuenta cuenta = cuentaRepository.findById(1L).orElseThrow();
        assertNotNull(cuenta);
        assertEquals("Nicolás", cuenta.getPersona());
        assertEquals("1000.00", cuenta.getSaldo().toPlainString());

        //When
        cuentaRepository.delete(cuenta);

        //Then
        assertThrows(NoSuchElementException.class, () -> cuentaRepository.findById(1L).orElseThrow());
        assertEquals(1, cuentaRepository.findAll().size());
    }
}
