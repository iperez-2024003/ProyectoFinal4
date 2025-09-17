package com.iversonperez.ProyectoFinalAhorcado.service;

import com.iversonperez.ProyectoFinalAhorcado.model.Partida;
import com.iversonperez.ProyectoFinalAhorcado.model.Palabra;
import com.iversonperez.ProyectoFinalAhorcado.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UserServiceImplements {
    Usuario save(Usuario usuario);
    List<Usuario> findAll();
    Usuario findByUsername(String username);
    Usuario validarUsuario(String username, String password);
    Optional<Usuario> findById(Integer id); // Nuevo
    Usuario actualizarUsuario(Integer id, Usuario usuarioActualizado); // Nuevo
    void eliminarUsuario(Integer id); // Nuevo

    // Métodos para el juego
    Palabra obtenerPalabraAleatoria();
    String obtenerPista(Integer partidaId, Integer numeroPista);
    Partida crearNuevaPartida(Integer usuarioId);
    Partida adivinarLetra(Integer partidaId, String letra);
    Partida obtenerPartidaPorId(Integer partidaId);
    Palabra obtenerPalabraPorId(Integer palabraId);
}