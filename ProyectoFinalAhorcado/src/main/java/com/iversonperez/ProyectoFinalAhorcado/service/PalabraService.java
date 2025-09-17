package com.iversonperez.ProyectoFinalAhorcado.service;

import com.iversonperez.ProyectoFinalAhorcado.model.Palabra;
import com.iversonperez.ProyectoFinalAhorcado.repository.PalabraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PalabraService {

    @Autowired
    private PalabraRepository palabraRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void agregarPalabra(Palabra palabra) {
        String sql = "CALL sp_insertar_palabra(?, ?, ?, ?)";
        jdbcTemplate.update(sql, palabra.getPalabra(), palabra.getPista1(), palabra.getPista2(), palabra.getPista3());
    }

    public List<Palabra> listarPalabras() {
        return palabraRepository.findAll();
    }

    public Optional<Palabra> findById(Integer id) {
        return palabraRepository.findById(id);
    }

    public Palabra actualizarPalabra(Integer id, Palabra palabraActualizada) {
        Optional<Palabra> optionalPalabra = palabraRepository.findById(id);
        if (optionalPalabra.isPresent()) {
            Palabra palabra = optionalPalabra.get();
            if (palabraActualizada.getPalabra() != null) palabra.setPalabra(palabraActualizada.getPalabra());
            if (palabraActualizada.getPista1() != null) palabra.setPista1(palabraActualizada.getPista1());
            if (palabraActualizada.getPista2() != null) palabra.setPista2(palabraActualizada.getPista2());
            if (palabraActualizada.getPista3() != null) palabra.setPista3(palabraActualizada.getPista3());
            return palabraRepository.save(palabra);
        }
        throw new RuntimeException("Palabra no encontrada");
    }

    public void eliminarPalabra(Integer id) {
        palabraRepository.deleteById(id);
    }
    }

