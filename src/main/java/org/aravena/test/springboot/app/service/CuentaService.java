package org.aravena.test.springboot.app.service;

import org.aravena.test.springboot.app.model.Cuenta;

import java.math.BigDecimal;

public interface CuentaService {
    Cuenta findById(Long id);
    int revisarTotalTransferencias(Long bancoID);
    BigDecimal revisarSaldo(Long cuentaID);
    void transferir(Long cuentaOrgen, Long cuentaDestino, BigDecimal monto, Long idBanco);
}
