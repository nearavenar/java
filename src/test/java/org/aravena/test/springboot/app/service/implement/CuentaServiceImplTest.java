package org.aravena.test.springboot.app.service.implement;

import org.aravena.test.springboot.app.model.Banco;
import org.aravena.test.springboot.app.model.Cuenta;
import org.aravena.test.springboot.app.repository.BancoRepository;
import org.aravena.test.springboot.app.repository.CuentaRepository;
import org.aravena.test.springboot.app.service.CuentaService;
import org.aravena.test.springboot.app.utils.Datos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

class CuentaServiceImplTest {

    CuentaRepository cuentaRepository;
    BancoRepository bancoRepository;
    CuentaService cuentaService;

    @BeforeEach
    void setUp() {
        cuentaRepository = mock(CuentaRepository.class);
        bancoRepository = mock(BancoRepository.class);
        cuentaService = new CuentaServiceImpl(cuentaRepository, bancoRepository);
    }

    @Test
    void findById() {
        when(cuentaRepository.findById(1L)).thenReturn(Datos.crearCuenta001());

        Cuenta cuenta = cuentaService.findById(1L);

        assertEquals(1, cuenta.getId());
        assertEquals("Nicolás", cuenta.getPersona());
        assertEquals("1000", cuenta.getSaldo().toPlainString());
        verify(cuentaRepository).findById(1L);
    }

    @Test
    void findAll() {
        //Given
        when(cuentaRepository.findAll()).thenReturn(Datos.listaCuentas());

        //When
        List<Cuenta> cuentas = cuentaService.findAll();

        //Then
        assertFalse(cuentas.isEmpty());
        assertEquals(5, cuentas.size());

        verify(cuentaRepository).findAll();
    }

    @Test
    void save() {
        Cuenta nueva = new Cuenta(null, "Jorge", new BigDecimal("40000"));
        when(cuentaRepository.save(any())).then(invocation -> {
            Cuenta c = invocation.getArgument(0);
            c.setId(6L);
            return c;
        });

        Cuenta cuenta = cuentaService.save(nueva);

        assertEquals(6L, cuenta.getId());
        assertEquals("Jorge", cuenta.getPersona());
        assertEquals("40000", cuenta.getSaldo().toPlainString());

        verify(cuentaRepository).save(any());
    }
}