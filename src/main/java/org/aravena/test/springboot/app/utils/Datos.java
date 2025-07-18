package org.aravena.test.springboot.app.utils;

import org.aravena.test.springboot.app.model.Banco;
import org.aravena.test.springboot.app.model.Cuenta;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
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

    public static List<Cuenta> listaCuentas(){
        return Arrays.asList(new Cuenta(1L, "Nicolás", new BigDecimal("1000")),
                             new Cuenta(2L, "Denisse", new BigDecimal("2000")),
                             new Cuenta(3L, "José", new BigDecimal("3000")),
                             new Cuenta(4L, "Patricia", new BigDecimal("4000")),
                             new Cuenta(5L, "David", new BigDecimal("5000")));
    }

    public static Optional<Banco> crearBancoFalabella(){
        return Optional.of(new Banco(1L, "Banco Falabella", 0));
    }
}
