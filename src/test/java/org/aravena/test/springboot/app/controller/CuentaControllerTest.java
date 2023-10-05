package org.aravena.test.springboot.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aravena.test.springboot.app.dto.TransaccionDto;
import org.aravena.test.springboot.app.model.Cuenta;
import org.aravena.test.springboot.app.service.CuentaService;
import org.aravena.test.springboot.app.utils.Datos;
import static org.hamcrest.Matchers.hasSize;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(CuentaController.class)
class CuentaControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CuentaService cuentaService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void detalleCuenta() throws Exception {
        //given
        when(cuentaService.findById(1L)).thenReturn(Datos.crearCuenta001().orElseThrow());
        //When
        mockMvc.perform(get("/api/cuentas/1").contentType(MediaType.APPLICATION_JSON))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(MockMvcResultMatchers.jsonPath("$.persona").value("Nicolás"))
               .andExpect(MockMvcResultMatchers.jsonPath("$.saldo").value("1000"));
        //Then
        verify(cuentaService).findById(1L);
    }

    @Test
    void transferir() throws Exception, JsonProcessingException {
        //Given
        TransaccionDto dto = new TransaccionDto();
        dto.setCuentaOrigen(1L);
        dto.setCuentaDestino(2L);
        dto.setMonto(new BigDecimal("100"));
        dto.setBancoId(1L);

        //When
        mockMvc.perform(post("/api/cuentas/transferir").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
        //Then
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.date").value(LocalDate.now().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("OK"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.transaccion.cuentaOrigen").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.transaccion.cuentaDestino").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.transaccion.bancoId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.transaccion.monto").value(100))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensaje").value("Transferencia realizada con éxito"));
    }

    @Test
    void transferirDos() throws Exception {
        //Given
        TransaccionDto dto = new TransaccionDto();
        dto.setCuentaOrigen(1L);
        dto.setCuentaDestino(2L);
        dto.setMonto(new BigDecimal("100"));
        dto.setBancoId(1L);

        System.out.println(objectMapper.writeValueAsString(dto));

        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status", "OK");
        response.put("mensaje", "Transferencia realizada con éxito");
        response.put("transaccion", dto);

        System.out.println(objectMapper.writeValueAsString(response));

        //When
        mockMvc.perform(post("/api/cuentas/transferir").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                //Then
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.date").value(LocalDate.now().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("OK"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.transaccion.cuentaOrigen").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.transaccion.cuentaDestino").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.transaccion.bancoId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.transaccion.monto").value(100))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensaje").value("Transferencia realizada con éxito"))
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void findAllTest() throws Exception {
        //Given
        when(cuentaService.findAll()).thenReturn(Datos.listaCuentas());

        //When
        mockMvc.perform(get("/api/cuentas").contentType(MediaType.APPLICATION_JSON))
        //Then
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].persona").value("Nicolás"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].saldo").value("1000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].persona").value("Denisse"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].saldo").value("2000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].persona").value("José"))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(5)))
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(Datos.listaCuentas())));
    }

    @Test
    void saveTest() throws Exception {
        Cuenta cuenta = new Cuenta(null, "Eugenio", new BigDecimal("3000"));
        when(cuentaService.save(any())).then(invocation -> {
            Cuenta c = invocation.getArgument(0);
            c.setId(3L);
            return c;
        });

        mockMvc.perform(post("/api/cuentas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cuenta)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.persona", is("Eugenio")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.saldo", is(3000)));

        verify(cuentaService).save(any());
    }
}