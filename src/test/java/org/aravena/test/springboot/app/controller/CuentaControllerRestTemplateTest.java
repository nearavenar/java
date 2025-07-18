package org.aravena.test.springboot.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aravena.test.springboot.app.dto.TransaccionDto;
import org.aravena.test.springboot.app.model.Cuenta;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

//@Tag("integracion_rt") -> ./mvnw test -Dgroups="!integracion_rt"
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CuentaControllerRestTemplateTest {

    @Autowired
    private TestRestTemplate client;

    private ObjectMapper objectMapper;

    @LocalServerPort
    private Integer puerto;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @Order(1)
    void transferirTest() {
        TransaccionDto dto = new TransaccionDto();
        dto.setCuentaOrigen(1L);
        dto.setCuentaDestino(2L);
        dto.setBancoId(1L);
        dto.setMonto(new BigDecimal("100"));

        //ResponseEntity<String> response =  client.postForEntity("http://localhost:"+puerto+"/api/cuentas/transferir", dto, String.class);
        ResponseEntity<String> response =  client.postForEntity("/api/cuentas/transferir", dto, String.class);
        String json = response.getBody();

        System.out.println(json);

        assertNotNull(json);
        assertTrue(json.contains("Transferencia realizada con éxito"));
        assertTrue(json.contains("\"cuentaOrigen\":1,\"cuentaDestino\":2,\"monto\":100,\"bancoId\":1"));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
    }

    @Test
    @Order(2)
    void transferir2Test() throws JsonProcessingException {
        TransaccionDto dto = new TransaccionDto();
        dto.setCuentaOrigen(1L);
        dto.setCuentaDestino(2L);
        dto.setBancoId(1L);
        dto.setMonto(new BigDecimal("100"));

        ResponseEntity<String> response =  client.postForEntity("/api/cuentas/transferir", dto, String.class);
        String json = response.getBody();

        assertNotNull(json);
        assertTrue(json.contains("Transferencia realizada con éxito"));
        assertTrue(json.contains("\"cuentaOrigen\":1,\"cuentaDestino\":2,\"monto\":100,\"bancoId\":1"));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());

        JsonNode jsonNode = objectMapper.readTree(json);
        assertEquals("Transferencia realizada con éxito", jsonNode.path("mensaje").asText());
        assertEquals(LocalDate.now().toString(), jsonNode.path("date").asText());
        assertEquals("100", jsonNode.path("transaccion").path("monto").asText());
        assertEquals(1L, jsonNode.path("transaccion").path("cuentaOrigen").asLong());
        assertEquals(2L, jsonNode.path("transaccion").path("cuentaDestino").asLong());
        assertEquals(1L, jsonNode.path("transaccion").path("bancoId").asLong());

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("date", LocalDate.now().toString());
        responseMap.put("status", "OK");
        responseMap.put("mensaje", "Transferencia realizada con éxito");
        responseMap.put("transaccion", dto);

        assertEquals(objectMapper.writeValueAsString(responseMap), json);
    }

    @Test
    @Order(3)
    void detalleTest() {
        ResponseEntity<Cuenta> response = client.getForEntity("/api/cuentas/1",Cuenta.class);

        Cuenta cuenta = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertNotNull(cuenta);
        assertEquals("Nicolás", cuenta.getPersona());
        assertEquals(1L, cuenta.getId());
        assertEquals("800.00", cuenta.getSaldo().toPlainString());
        assertEquals(new Cuenta(1L, "Nicolás", new BigDecimal("800.00")), cuenta);
    }

    @Test
    @Order(4)
    void listarTest() {
        ResponseEntity<Cuenta[]> response = client.getForEntity("/api/cuentas/", Cuenta[].class);
        List<Cuenta> cuentas = Arrays.asList(response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertNotNull(cuentas);
        assertEquals(2, cuentas.size());
        assertEquals("Nicolás", cuentas.get(0).getPersona());
        assertEquals(1L, cuentas.get(0).getId());
        assertEquals("800.00", cuentas.get(0).getSaldo().toPlainString());
        assertEquals("Denisse", cuentas.get(1).getPersona());
        assertEquals(2L, cuentas.get(1).getId());
        assertEquals("2200.00", cuentas.get(1).getSaldo().toPlainString());
    }

    @Test
    @Order(5)
    void listar2Test() throws JsonProcessingException {
        ResponseEntity<Cuenta[]> response = client.getForEntity("/api/cuentas/", Cuenta[].class);
        List<Cuenta> cuentas = Arrays.asList(response.getBody());

        JsonNode json = objectMapper.readTree(objectMapper.writeValueAsString(cuentas));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertNotNull(json);
        assertEquals(2, cuentas.size());
        assertEquals(1L, json.get(0).path("id").asLong());
        assertEquals("Nicolás", json.get(0).path("persona").asText());
        assertEquals("800.0", json.get(0).path("saldo").asText());
        assertEquals(2L, json.get(1).path("id").asLong());
        assertEquals("Denisse", json.get(1).path("persona").asText());
        assertEquals("2200.0", json.get(1).path("saldo").asText());
    }

    @Test
    @Order(6)
    void guardatTest() {
        Cuenta cuenta = new Cuenta(null, "Pepe", new BigDecimal("3500"));
        ResponseEntity<Cuenta> response = client.postForEntity("/api/cuentas/", cuenta, Cuenta.class);
        Cuenta cuentaCreada = response.getBody();

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertNotNull(cuentaCreada);
        assertEquals(3L, cuentaCreada.getId());
        assertEquals("Pepe", cuentaCreada.getPersona());
        assertEquals("3500", cuentaCreada.getSaldo().toPlainString());
    }

    @Test
    @Order(7)
    void eliminarTest() {
        ResponseEntity<Cuenta[]> response = client.getForEntity("/api/cuentas/", Cuenta[].class);
        List<Cuenta> cuentas = Arrays.asList(response.getBody());
        assertEquals(3, cuentas.size());

        client.delete("/api/cuentas/3");

        response = client.getForEntity("/api/cuentas/", Cuenta[].class);
        cuentas = Arrays.asList(response.getBody());
        assertEquals(2, cuentas.size());

        ResponseEntity<Cuenta> responseDetalle = client.getForEntity("/api/cuentas/3",Cuenta.class);
        assertEquals(HttpStatus.NOT_FOUND, responseDetalle.getStatusCode());
        assertFalse(responseDetalle.hasBody());

    }

    @Test
    @Order(8)
    void eliminar2Test() {
        Map<String, Long> pathVariable = new HashMap<>();
        pathVariable.put("id", 2L);

        ResponseEntity<Cuenta[]> response = client.getForEntity("/api/cuentas/", Cuenta[].class);
        List<Cuenta> cuentas = Arrays.asList(response.getBody());
        assertEquals(2, cuentas.size());

        ResponseEntity<Void> responseDetete = client.exchange("/api/cuentas/{id}", HttpMethod.DELETE, null, Void.class, pathVariable);

        assertEquals(HttpStatus.NO_CONTENT, responseDetete.getStatusCode());

        response = client.getForEntity("/api/cuentas/", Cuenta[].class);
        cuentas = Arrays.asList(response.getBody());
        assertEquals(1, cuentas.size());

        ResponseEntity<Cuenta> responseDetalle = client.exchange("/api/cuentas/{id}", HttpMethod.GET, null, Cuenta.class, pathVariable);
        assertEquals(HttpStatus.NOT_FOUND, responseDetalle.getStatusCode());
        assertFalse(responseDetalle.hasBody());

    }
}