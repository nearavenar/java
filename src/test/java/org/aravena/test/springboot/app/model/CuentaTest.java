package org.aravena.test.springboot.app.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CuentaTest {

    private Cuenta cuenta;

    @BeforeEach
    void setUp() {
        cuenta = new Cuenta();
        cuenta.setId(1L);
        cuenta.setPersona("Nicolas");
        cuenta.setSaldo(new BigDecimal("3000"));
    }

    @Test
    void getAtrubutesTest() {
        assertEquals(1L, cuenta.getId());
        assertEquals("Nicolas", cuenta.getPersona());
        assertEquals("3000", cuenta.getSaldo().toPlainString());
    }

    @Test
    void equalsTest() {

        Cuenta cuenta2 = new Cuenta();
        cuenta2.setId(1L);
        cuenta2.setPersona("Nicolas");
        cuenta2.setSaldo(new BigDecimal("3000"));
        assertTrue(cuenta.equals(cuenta2));
    }

    @Test
    void hashCodeTest() {
        assertEquals(139093393, cuenta.hashCode());
    }
}