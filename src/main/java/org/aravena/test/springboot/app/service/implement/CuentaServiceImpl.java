package org.aravena.test.springboot.app.service.implement;

import org.aravena.test.springboot.app.model.Banco;
import org.aravena.test.springboot.app.model.Cuenta;
import org.aravena.test.springboot.app.repository.BancoRepository;
import org.aravena.test.springboot.app.repository.CuentaRepository;
import org.aravena.test.springboot.app.service.CuentaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CuentaServiceImpl implements CuentaService {

    private CuentaRepository cuentaRepository;
    private BancoRepository bancoRepository;

    //Constructor ara inyectar por mockito
    public CuentaServiceImpl(CuentaRepository cuentaRepository, BancoRepository bancoRepository) {
        this.cuentaRepository = cuentaRepository;
        this.bancoRepository = bancoRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public Cuenta findById(Long id) {
        return cuentaRepository.findById(id).orElseThrow();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cuenta> findAll() {
        return cuentaRepository.findAll();
    }

    @Override
    @Transactional()
    public Cuenta save(Cuenta cuenta) {
        return cuentaRepository.save(cuenta);
    }

    @Override
    @Transactional(readOnly = true)
    public int revisarTotalTransferencias(Long bancoID) {
        Banco banco = bancoRepository.findById(bancoID).orElseThrow();
        return banco.getTotalTransferencias();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal revisarSaldo(Long cuentaID) {
        Cuenta cuenta = cuentaRepository.findById(cuentaID).orElseThrow();
        return cuenta.getSaldo();
    }

    @Override
    @Transactional()
    public void transferir(Long cuentaOrgen, Long cuentaDestino, BigDecimal monto, Long idBanco) {
        Cuenta cuentaDeOrgen = cuentaRepository.findById(cuentaOrgen).orElseThrow();
        cuentaDeOrgen.debito(monto);
        cuentaRepository.save(cuentaDeOrgen);

        Cuenta cuentaDeDestino = cuentaRepository.findById(cuentaDestino).orElseThrow();
        cuentaDeDestino.credito(monto);
        cuentaRepository.save(cuentaDeDestino);

        Banco banco = bancoRepository.findById(idBanco).orElseThrow();
        int totalTransferencias = banco.getTotalTransferencias();
        banco.setTotalTransferencias(++totalTransferencias);
        bancoRepository.save(banco);
    }
}
