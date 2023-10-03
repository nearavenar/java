package org.aravena.test.springboot.app.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TransaccionDtoTest {

    private TransaccionDto dto;

    @BeforeEach
    void setUp() {
        dto = new TransaccionDto();
        dto.setCuentaOrigen(1L);
        dto.setCuentaDestino(2L);
        dto.setMonto(new BigDecimal("2000"));
        dto.setBancoId(1L);
    }

    @Test
    void toStringTest() {
        String value = "TransaccionDto{cuentaOrigen=1, cuentaDestino=2, monto=2000, bancoId=1}";
        assertEquals(value, dto.toString());
    }
}