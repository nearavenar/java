package org.aravena.test.springboot.app.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class BancoTest {

    private Banco banco;

    @BeforeEach
    void setUp() {
        banco = new Banco();
        banco.setId(1L);
        banco.setNombre("Credi Chile");
        banco.setTotalTransferencias(5);
    }

    @Test
    void getAtrubutesTest() {
        assertEquals(1L, banco.getId());
        assertEquals("Credi Chile", banco.getNombre());
        assertEquals(5, banco.getTotalTransferencias());
    }


    @Test
    void testEquals() {
        Banco banco2 = new Banco();
        banco2.setId(1L);
        banco2.setNombre("Credi Chile");
        banco2.setTotalTransferencias(5);
        assertTrue(banco.equals(banco2));
    }

    @Test
    void testHashCode() {
        assertEquals(1299038669, banco.hashCode());
    }
}