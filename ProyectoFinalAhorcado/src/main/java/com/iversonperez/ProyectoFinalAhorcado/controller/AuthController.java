package com.iversonperez.ProyectoFinalAhorcado.controller;

import com.iversonperez.ProyectoFinalAhorcado.model.Usuario;
import com.iversonperez.ProyectoFinalAhorcado.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserService userService;

    // POST: Login
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> credentials) {
        Map<String, Object> response = new HashMap<>();

        try {
            String username = credentials.get("username");
            String password = credentials.get("password");

            Usuario usuario = userService.validarUsuario(username, password);

            if (usuario != null) {
                response.put("success", true);
                response.put("mensaje", "Login exitoso");
                response.put("usuario", Map.of(
                        "id", usuario.getId(),
                        "username", usuario.getUsername(),
                        "email", usuario.getEmail(),
                        "partidasJugadas", usuario.getPartidasJugadas(),
                        "partidasGanadas", usuario.getPartidasGanadas()
                ));
            } else {
                response.put("success", false);
                response.put("mensaje", "Credenciales inválidas");
            }

        } catch (Exception e) {
            response.put("success", false);
            response.put("mensaje", "Error en login: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    // POST: Registro
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, String> userData) {
        Map<String, Object> response = new HashMap<>();

        try {
            String username = userData.get("username");
            String email = userData.get("email");
            String password = userData.get("password");

            // Validaciones básicas
            if (username == null || username.trim().isEmpty()) {
                response.put("success", false);
                response.put("mensaje", "El username es requerido");
                return ResponseEntity.badRequest().body(response);
            }

            if (email == null || email.trim().isEmpty()) {
                response.put("success", false);
                response.put("mensaje", "El email es requerido");
                return ResponseEntity.badRequest().body(response);
            }

            if (password == null || password.length() < 6) {
                response.put("success", false);
                response.put("mensaje", "La contraseña debe tener al menos 6 caracteres");
                return ResponseEntity.badRequest().body(response);
            }

            // Verificar si ya existe
            if (userService.findByUsername(username) != null) {
                response.put("success", false);
                response.put("mensaje", "El username ya existe");
                return ResponseEntity.badRequest().body(response);
            }

            // Crear nuevo usuario
            Usuario nuevoUsuario = new Usuario(username, email, password);
            Usuario usuarioGuardado = userService.save(nuevoUsuario);

            response.put("success", true);
            response.put("mensaje", "Usuario registrado exitosamente");
            response.put("usuario", Map.of(
                    "id", usuarioGuardado.getId(),
                    "username", usuarioGuardado.getUsername(),
                    "email", usuarioGuardado.getEmail()
            ));

        } catch (Exception e) {
            response.put("success", false);
            response.put("mensaje", "Error en registro: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    // GET: Obtener perfil de usuario
    @GetMapping("/profile/{userId}")
    public ResponseEntity<Map<String, Object>> getUserProfile(@PathVariable Integer userId) {
        Map<String, Object> response = new HashMap<>();

        try {
            Usuario usuario = userService.findAll().stream()
                    .filter(u -> u.getId().equals(userId))
                    .findFirst()
                    .orElse(null);

            if (usuario != null) {
                response.put("success", true);
                response.put("usuario", Map.of(
                        "id", usuario.getId(),
                        "username", usuario.getUsername(),
                        "email", usuario.getEmail(),
                        "partidasJugadas", usuario.getPartidasJugadas(),
                        "partidasGanadas", usuario.getPartidasGanadas(),
                        "porcentajeVictoria", usuario.getPartidasJugadas() > 0 ?
                                (double) usuario.getPartidasGanadas() / usuario.getPartidasJugadas() * 100 : 0
                ));
            } else {
                response.put("success", false);
                response.put("mensaje", "Usuario no encontrado");
            }

        } catch (Exception e) {
            response.put("success", false);
            response.put("mensaje", "Error al obtener perfil: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }
}