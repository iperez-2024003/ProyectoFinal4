package com.iversonperez.ProyectoFinalAhorcado.service;

import com.iversonperez.ProyectoFinalAhorcado.model.Partida;
import com.iversonperez.ProyectoFinalAhorcado.model.Palabra;
import com.iversonperez.ProyectoFinalAhorcado.model.Usuario;

import java.util.List;

public interface UserServiceImplements {
    Usuario save(Usuario usuario);
    List<Usuario> findAll();
    Usuario findByUsername(String username);
    Usuario validarUsuario(String username, String password);

    // Métodos para el juego
    Palabra obtenerPalabraAleatoria();
    String obtenerPista(Integer partidaId, Integer numeroPista);
    Partida crearNuevaPartida(Integer usuarioId);
    Partida adivinarLetra(Integer partidaId, String letra);
    Partida obtenerPartidaPorId(Integer partidaId);
    Palabra obtenerPalabraPorId(Integer palabraId);
}