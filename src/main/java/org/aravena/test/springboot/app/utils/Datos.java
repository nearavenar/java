package org.aravena.test.springboot.app.utils;

import org.aravena.test.springboot.app.model.Banco;
import org.aravena.test.springboot.app.model.Cuenta;

import java.math.BigDecimal;
import java.util.Optional;

public class Datos {
    public static final  Cuenta CUENTA_001 = new Cuenta(1L, "Nicolás", new BigDecimal("1000"));
    public static final  Cuenta CUENTA_002 = new Cuenta(2L, "Denisse", new BigDecimal("2000"));
    public static final Banco BANCO_FALABELLA = new Banco(1L, "Banco Falabella", 0);
    public static final Banco BANCO_ESTADO = new Banco(2L, "Banco Estado", 0);

    public static Optional<Cuenta> crearCuenta001(){
        return Optional.of(new Cuenta(1L, "Nicolás", new BigDecimal("1000")));
    }
    public static  Optional<Cuenta> crearCuenta002(){
        return Optional.of(new Cuenta(2L, "Denisse", new BigDecimal("2000")));
    }

    public static Optional<Banco> crearBancoFalabella(){
        return Optional.of(new Banco(1L, "Banco Falabella", 0));
    }
}
