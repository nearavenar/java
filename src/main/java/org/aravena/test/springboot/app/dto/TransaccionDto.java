package org.aravena.test.springboot.app.dto;

import java.math.BigDecimal;

public class TransaccionDto {
    private Long cuentaOrigen;
    private Long cuentaDestino;
    private BigDecimal monto;

    private Long bancoId;

    public Long getCuentaOrigen() {
        return cuentaOrigen;
    }

    public void setCuentaOrigen(Long cuentaOrigen) {
        this.cuentaOrigen = cuentaOrigen;
    }

    public Long getCuentaDestino() {
        return cuentaDestino;
    }

    public void setCuentaDestino(Long cuentaDestino) {
        this.cuentaDestino = cuentaDestino;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public Long getBancoId() {
        return bancoId;
    }

    public void setBancoId(Long bancoId) {
        this.bancoId = bancoId;
    }

    @Override
    public String toString() {
        return "TransaccionDto{" +
                "cuentaOrigen=" + cuentaOrigen +
                ", cuentaDestino=" + cuentaDestino +
                ", monto=" + monto +
                ", bancoId=" + bancoId +
                '}';
    }
}
