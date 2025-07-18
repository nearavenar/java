package org.aravena.test.springboot.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aravena.test.springboot.app.dto.TransaccionDto;
import org.aravena.test.springboot.app.model.Cuenta;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import static org.junit.jupiter.api.Assertions.*;

//@Tag("integracion_wc")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) //Pruebas de integracion a controladores con WebTestClient
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CuentaControllerWebClientTest {

    @Autowired
    private WebTestClient client;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @Order(1)
    void transferirTest() throws JsonProcessingException {
        //Given
        TransaccionDto dto = new TransaccionDto();
        dto.setCuentaOrigen(1L);
        dto.setCuentaDestino(2L);
        dto.setBancoId(1L);
        dto.setMonto(new BigDecimal("100"));

        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status", "OK");
        response.put("mensaje", "Transferencia realizada con éxito");
        response.put("transaccion", dto);

        //When
        client.post()
                .uri("/api/cuentas/transferir")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
        //Then
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.mensaje").isNotEmpty()
                .jsonPath("$.mensaje").value(is("Transferencia realizada con éxito"))
                .jsonPath("$.mensaje").value(valor -> assertEquals("Transferencia realizada con éxito", valor))
                .jsonPath("$.mensaje").isEqualTo("Transferencia realizada con éxito")
                .jsonPath("$.transaccion.cuentaOrigen").isEqualTo(dto.getCuentaOrigen())
                .jsonPath("$.date").isEqualTo(LocalDate.now().toString())

                .json(objectMapper.writeValueAsString(response));
    }

    @Order(2)
    @Test
    void detalleTest() throws JsonProcessingException {

        Cuenta cuenta = new Cuenta(1L, "Nicolás", new BigDecimal("900"));

        client.get().uri("/api/cuentas/1")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.persona").isEqualTo("Nicolás")
                .jsonPath("$.saldo").isEqualTo(900)
                .jsonPath(objectMapper.writeValueAsString(cuenta));
    }

    @Test
    @Order(3)
    void detalleDosTest() {
        client.get().uri("/api/cuentas/2")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Cuenta.class)
                .consumeWith(response -> {
                    Cuenta cuenta = response.getResponseBody();
                    assertEquals("Denisse", cuenta.getPersona());
                    assertEquals("2100.00", cuenta.getSaldo().toPlainString());
                });
    }

    @Test
    @Order(4)
    void listarTest() {
        client.get().uri("/api/cuentas/")
              .exchange()
              .expectHeader().contentType(MediaType.APPLICATION_JSON)
              .expectBody()
              .jsonPath("$[0].persona").isEqualTo("Nicolás")
              .jsonPath("$[0].id").isEqualTo(1L)
              .jsonPath("$[0].saldo").isEqualTo(900)

              .jsonPath("$[1].persona").isEqualTo("Denisse")
              .jsonPath("$[1].id").isEqualTo(2L)
              .jsonPath("$[1].saldo").isEqualTo(2100)

              .jsonPath("$").isArray()
              .jsonPath("$").value(hasSize(2));
    }

    @Test
    @Order(5)
    void listarDosTest() {
        client.get().uri("/api/cuentas/")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Cuenta.class)
                .consumeWith(response -> {
                    List<Cuenta> cuentas = response.getResponseBody();
                    assertNotNull(cuentas);
                    assertEquals(2, cuentas.size());

                    assertEquals(1L, cuentas.get(0).getId());
                    assertEquals("Nicolás", cuentas.get(0).getPersona());
                    assertEquals(900, cuentas.get(0).getSaldo().intValue());

                    assertEquals(2L, cuentas.get(1).getId());
                    assertEquals("Denisse", cuentas.get(1).getPersona());
                    assertEquals(2100, cuentas.get(1).getSaldo().intValue());
                })
                .hasSize(2);
    }

    @Test
    @Order(6)
    void guardarTest() {
        //Given
        Cuenta cuenta = new Cuenta(null, "Juan", new BigDecimal("3000"));

        //When
        client.post().uri("/api/cuentas/").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(cuenta)
                .exchange()
        //Then
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.persona").isEqualTo("Juan")
                .jsonPath("$.id").isEqualTo(3L)
                .jsonPath("$.saldo").isEqualTo(3000);
    }

    @Test
    @Order(7)
    void guardarDosTest() {
        //Given
        Cuenta cuenta = new Cuenta(null, "Pepe", new BigDecimal("3500"));

        //When
        client.post().uri("/api/cuentas/").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(cuenta)
                .exchange()
         //Then
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Cuenta.class)
                .consumeWith(response -> {
                    Cuenta c = response.getResponseBody();
                    assertNotNull(c);
                    assertEquals(4L, c.getId());
                    assertEquals("Pepe", c.getPersona());
                    assertEquals(3500, c.getSaldo().intValue());
                });
    }

    @Test
    @Order(8)
    void eliminarTest() {

        client.get().uri("/api/cuentas/").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Cuenta.class)
                .hasSize(4);

        client.delete().uri("/api/cuentas/3").exchange()
                .expectStatus().isNoContent()
                .expectBody().isEmpty();

        client.get().uri("/api/cuentas/").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Cuenta.class)
                .hasSize(3);

        /*client.get().uri("/api/cuentas/3").exchange()
                .expectStatus().is5xxServerError();*/

        client.get().uri("/api/cuentas/3").exchange()
                .expectStatus().isNotFound()
                .expectBody().isEmpty();
    }
}