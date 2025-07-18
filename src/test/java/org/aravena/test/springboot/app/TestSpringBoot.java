package org.aravena.test.springboot.app;

import org.aravena.test.springboot.app.model.Cuenta;
import org.aravena.test.springboot.app.repository.BancoRepository;
import org.aravena.test.springboot.app.repository.CuentaRepository;
import org.aravena.test.springboot.app.service.CuentaService;
import org.aravena.test.springboot.app.utils.Datos;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TestSpringBoot {
    @MockBean
    CuentaRepository cuentaRepository;
    @MockBean
    BancoRepository bancoRepository;
    @Autowired
    CuentaService cuentaService; //Debe ser un componente de spring @Service

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
