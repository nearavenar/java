package org.aravena.test.springboot.app;

import org.aravena.test.springboot.app.model.Cuenta;
import org.aravena.test.springboot.app.repository.CuentaRepository;
import org.aravena.test.springboot.app.service.implement.CuentaServiceImpl;
import org.aravena.test.springboot.app.utils.Datos;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;//import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

//@SpringBootTest
@ExtendWith(SpringExtension.class)
public class TestMockito {
    @Mock
    CuentaRepository cuentaRepository;
    @InjectMocks
    CuentaServiceImpl cuentaService;

    @Test
    void contextLoads() {
        when(cuentaRepository.findById(1L)).thenReturn(Datos.crearCuenta001());

        Cuenta cuentaUno = cuentaService.findById(1L);
        Cuenta cuentaDos = cuentaService.findById(1L);

        assertSame(cuentaUno, cuentaDos);
        assertTrue(cuentaUno == cuentaDos);
        assertEquals("Nicolás", cuentaUno.getPersona());
        assertEquals("Nicolás", cuentaDos.getPersona());

        verify(cuentaRepository, times(2)).findById(anyLong());
    }
}
