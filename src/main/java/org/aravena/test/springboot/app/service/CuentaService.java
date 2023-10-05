package org.aravena.test.springboot.app.service;

import org.aravena.test.springboot.app.model.Cuenta;

import java.math.BigDecimal;
import java.util.List;

public interface CuentaService {
    List<Cuenta> findAll();
    Cuenta findById(Long id);

    Cuenta save(Cuenta cuenta);
    int revisarTotalTransferencias(Long bancoID);
    BigDecimal revisarSaldo(Long cuentaID);
    void transferir(Long cuentaOrgen, Long cuentaDestino, BigDecimal monto, Long idBanco);
}
