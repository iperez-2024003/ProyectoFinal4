package com.iversonperez.ProyectoFinalAhorcado.repository;

import com.iversonperez.ProyectoFinalAhorcado.model.Palabra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PalabraRepository extends JpaRepository<Palabra, Integer> {
}