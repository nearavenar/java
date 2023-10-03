package org.aravena.test.springboot.app.repository;

import org.aravena.test.springboot.app.model.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
    @Query("SELECT c FROM Cuenta c WHERE c.persona = :persona")
    Optional<Cuenta> findByPersona(String persona);

    //List<Cuenta> findAll();
    //Optional<Cuenta> findById(Long id);
    //void update(Cuenta cuenta);
}
