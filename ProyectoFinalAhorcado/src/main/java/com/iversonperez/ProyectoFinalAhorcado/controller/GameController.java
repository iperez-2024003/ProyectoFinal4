package com.iversonperez.ProyectoFinalAhorcado.controller;

import com.iversonperez.ProyectoFinalAhorcado.model.Partida;
import com.iversonperez.ProyectoFinalAhorcado.model.Palabra;
import com.iversonperez.ProyectoFinalAhorcado.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/game")
@CrossOrigin(origins = "*")
public class GameController {

    @Autowired
    private UserService userService;

    // GET: Crear nueva partida
    @GetMapping("/nueva/{usuarioId}")
    public ResponseEntity<Map<String, Object>> crearNuevaPartida(@PathVariable Integer usuarioId) {
        Map<String, Object> response = new HashMap<>();

        try {
            Partida partida = userService.crearNuevaPartida(usuarioId);
            Palabra palabra = userService.obtenerPalabraPorId(partida.getPalabraId());

            response.put("success", true);
            response.put("partidaId", partida.getId());
            response.put("palabraOculta", generarPalabraOculta(partida.getPalabra(), partida.getLetrasAdivinadas()));
            response.put("intentosRestantes", partida.getMaxIntentos() - partida.getIntentosFallidos());
            response.put("letrasIncorrectas", partida.getLetrasIncorrectas().split(""));
            response.put("estado", partida.getEstado().toString());
            response.put("pista1", palabra.getPista1());
            response.put("mensaje", "Nueva partida creada exitosamente");

        } catch (Exception e) {
            response.put("success", false);
            response.put("mensaje", "Error al crear partida: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    // POST: Adivinar letra
    @PostMapping("/adivinar")
    public ResponseEntity<Map<String, Object>> adivinarLetra(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();

        try {
            Integer partidaId = (Integer) request.get("partidaId");
            String letra = ((String) request.get("letra")).toUpperCase();

            Partida partida = userService.adivinarLetra(partidaId, letra);
            Palabra palabra = userService.obtenerPalabraPorId(partida.getPalabraId());

            response.put("success", true);
            response.put("palabraOculta", generarPalabraOculta(partida.getPalabra(), partida.getLetrasAdivinadas()));
            response.put("intentosRestantes", partida.getMaxIntentos() - partida.getIntentosFallidos());
            response.put("letrasIncorrectas", partida.getLetrasIncorrectas().split(""));
            response.put("estado", partida.getEstado().toString());
            response.put("letraCorrecta", partida.getPalabra().contains(letra));

            if (partida.getEstado().toString().equals("GANADA")) {
                response.put("mensaje", "¡Felicitaciones! Has ganado la partida.");
                response.put("palabraCompleta", partida.getPalabra());
            } else if (partida.getEstado().toString().equals("PERDIDA")) {
                response.put("mensaje", "Game Over. La palabra era: " + partida.getPalabra());
                response.put("palabraCompleta", partida.getPalabra());
            }

        } catch (Exception e) {
            response.put("success", false);
            response.put("mensaje", "Error al procesar letra: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    // GET: Obtener pista
    @GetMapping("/pista/{partidaId}/{numeroPista}")
    public ResponseEntity<Map<String, Object>> obtenerPista(@PathVariable Integer partidaId, @PathVariable Integer numeroPista) {
        Map<String, Object> response = new HashMap<>();

        try {
            String pista = userService.obtenerPista(partidaId, numeroPista);
            response.put("success", true);
            response.put("pista", pista);
            response.put("mensaje", "Pista " + numeroPista + " obtenida");

        } catch (Exception e) {
            response.put("success", false);
            response.put("mensaje", "Error al obtener pista: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    // GET: Estado de partida
    @GetMapping("/estado/{partidaId}")
    public ResponseEntity<Map<String, Object>> obtenerEstadoPartida(@PathVariable Integer partidaId) {
        Map<String, Object> response = new HashMap<>();

        try {
            Partida partida = userService.obtenerPartidaPorId(partidaId);

            response.put("success", true);
            response.put("palabraOculta", generarPalabraOculta(partida.getPalabra(), partida.getLetrasAdivinadas()));
            response.put("intentosRestantes", partida.getMaxIntentos() - partida.getIntentosFallidos());
            response.put("letrasIncorrectas", partida.getLetrasIncorrectas().split(""));
            response.put("estado", partida.getEstado().toString());
            response.put("pistasUsadas", partida.getPistasUsadas());

        } catch (Exception e) {
            response.put("success", false);
            response.put("mensaje", "Error al obtener estado: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    private String generarPalabraOculta(String palabra, String letrasAdivinadas) {
        StringBuilder palabraOculta = new StringBuilder();
        for (char c : palabra.toCharArray()) {
            if (letrasAdivinadas.contains(String.valueOf(c))) {
                palabraOculta.append(c).append(" ");
            } else {
                palabraOculta.append("_ ");
            }
        }
        return palabraOculta.toString().trim();
    }
}