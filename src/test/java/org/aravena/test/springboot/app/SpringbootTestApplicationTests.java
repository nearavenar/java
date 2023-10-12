package org.aravena.test.springboot.app;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.aravena.test.springboot.app.exception.DineroInsuficienteException;
import org.aravena.test.springboot.app.model.Banco;
import org.aravena.test.springboot.app.model.Cuenta;
import org.aravena.test.springboot.app.repository.BancoRepository;
import org.aravena.test.springboot.app.repository.CuentaRepository;
import org.aravena.test.springboot.app.service.CuentaService;
import org.aravena.test.springboot.app.service.implement.CuentaServiceImpl;
import org.aravena.test.springboot.app.utils.Datos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest //Mock MVC
class SpringbootTestApplicationTests {

	CuentaRepository cuentaRepository;
	BancoRepository bancoRepository;
	CuentaService cuentaService;

	@BeforeEach
	void setUp() {
		cuentaRepository = mock(CuentaRepository.class);
		bancoRepository = mock(BancoRepository.class);
		cuentaService = new CuentaServiceImpl(cuentaRepository, bancoRepository);

		//Datos.CUENTA_001.setSaldo(new BigDecimal("1000"));
		//Datos.CUENTA_002.setSaldo(new BigDecimal("2000"));
		//Datos.BANCO_FALABELLA.setTotalTransferencias(0);
	}

	@Test
	void contextLoads() {
		//Given -> Dado un contecto
		when(cuentaRepository.findById(1L)).thenReturn(Datos.crearCuenta001());
		when(cuentaRepository.findById(2L)).thenReturn(Datos.crearCuenta002());
		when(bancoRepository.findById(1L)).thenReturn(Datos.crearBancoFalabella());

		//Then -> Entonces
		BigDecimal saldoOrigen = cuentaService.revisarSaldo(1L);
		BigDecimal saldoDestino = cuentaService.revisarSaldo(2L);

		assertEquals("1000", saldoOrigen.toPlainString());
		assertEquals("2000", saldoDestino.toPlainString());

		cuentaService.transferir(1L,2L, new BigDecimal("100"), 1L);

		saldoOrigen = cuentaService.revisarSaldo(1L);
		saldoDestino = cuentaService.revisarSaldo(2L);

		assertEquals("900", saldoOrigen.toPlainString());
		assertEquals("2100", saldoDestino.toPlainString());

		int total = cuentaService.revisarTotalTransferencias(1L);

		assertEquals(1, total);

		verify(cuentaRepository, times(3)).findById(1L);
		verify(cuentaRepository, times(3)).findById(2L);
		verify(cuentaRepository, times(2)).save(any(Cuenta.class));

		verify(bancoRepository, times(2)).findById(1L);
		verify(bancoRepository).save(any(Banco.class));

		verify(cuentaRepository, times(6)).findById(anyLong());
		verify(cuentaRepository, never()).findAll();
	}

	@Test
	void contextLoadsDos() {
		//Given -> Dado un contecto
		when(cuentaRepository.findById(1L)).thenReturn(Datos.crearCuenta001());
		when(cuentaRepository.findById(2L)).thenReturn(Datos.crearCuenta002());
		when(bancoRepository.findById(1L)).thenReturn(Datos.crearBancoFalabella());

		//Then -> Entonces
		BigDecimal saldoOrigen = cuentaService.revisarSaldo(1L);
		BigDecimal saldoDestino = cuentaService.revisarSaldo(2L);

		assertEquals("1000", saldoOrigen.toPlainString());
		assertEquals("2000", saldoDestino.toPlainString());


		assertThrows(DineroInsuficienteException.class, () ->{
			cuentaService.transferir(1L,2L, new BigDecimal("1200"), 1L);
		});

		saldoOrigen = cuentaService.revisarSaldo(1L);
		saldoDestino = cuentaService.revisarSaldo(2L);

		assertEquals("1000", saldoOrigen.toPlainString());
		assertEquals("2000", saldoDestino.toPlainString());

		int total = cuentaService.revisarTotalTransferencias(1L);

		assertEquals(0, total);

		verify(cuentaRepository, times(3)).findById(1L);
		verify(cuentaRepository, times(2)).findById(2L);
		verify(cuentaRepository, never()).save(any(Cuenta.class));

		verify(bancoRepository, times(1)).findById(1L);
		verify(bancoRepository, never()).save(any(Banco.class));
		verify(cuentaRepository, times(5)).findById(anyLong());
		verify(cuentaRepository, never()).findAll();
	}

	@Test
	void contextLoadsTres() {
		when(cuentaRepository.findById(1L)).thenReturn(Datos.crearCuenta001());

		Cuenta cuentaUno = cuentaService.findById(1L);
		Cuenta cuentaDos = cuentaService.findById(1L);

		assertSame(cuentaUno, cuentaDos);
		assertTrue(cuentaUno == cuentaDos);
		assertEquals("Nicolás", cuentaUno.getPersona());
		assertEquals("Nicolás", cuentaDos.getPersona());

		verify(cuentaRepository, times(2)).findById(anyLong());
	}

	@Test
	void findAllTest() {
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
	void saveTest() {
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
