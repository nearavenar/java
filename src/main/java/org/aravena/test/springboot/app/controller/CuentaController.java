package org.aravena.test.springboot.app.controller;

import org.aravena.test.springboot.app.dto.TransaccionDto;
import org.aravena.test.springboot.app.model.Cuenta;
import org.aravena.test.springboot.app.service.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {

    @Autowired
    CuentaService cuentaService;

    @GetMapping("/{id}")//Path variable
    @ResponseStatus(HttpStatus.OK)
    public Cuenta detalleCuenta(@PathVariable(name = "id") Long id){
        return cuentaService.findById(id);
    }

    @PostMapping("/transferir")
    public ResponseEntity<?> transferir(@RequestBody TransaccionDto cuenta){
        cuentaService.transferir(cuenta.getCuentaOrigen(), cuenta.getCuentaDestino(), cuenta.getMonto(), cuenta.getBancoId());

        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status", "OK");
        response.put("mensaje", "Transferencia realizada con éxito");
        response.put("transaccion", cuenta);

        return ResponseEntity.ok().body(response);
    }
}
