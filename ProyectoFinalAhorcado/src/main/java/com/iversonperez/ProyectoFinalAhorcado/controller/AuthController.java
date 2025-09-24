package com.iversonperez.ProyectoFinalAhorcado.controller;

import com.iversonperez.ProyectoFinalAhorcado.model.Usuario;
import com.iversonperez.ProyectoFinalAhorcado.service.UserService;
import com.iversonperez.ProyectoFinalAhorcado.service.Validacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private Validacion validacion;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> credentials) {
        Map<String, Object> response = new HashMap<>();

        try {
            String username = credentials.get("username");
            String password = credentials.get("password");

            // Validar credenciales básicas
            String errorValidacion = validacion.validarCredencialesLogin(username, password);
            if (errorValidacion != null) {
                throw new RuntimeException(errorValidacion);
            }

            // Intentar validar usuario
            Usuario usuario = userService.validarUsuario(username.trim(), password);

            if (usuario != null) {
                response.put("success", true);
                response.put("mensaje", "Login exitoso");
                response.put("usuario", Map.of(
                        "id", usuario.getId(),
                        "username", usuario.getUsername(),
                        "partidasJugadas", usuario.getPartidasJugadas(),
                        "partidasGanadas", usuario.getPartidasGanadas()
                ));
                return ResponseEntity.ok(response);
            } else {
                throw new RuntimeException("Credenciales invalidas");
            }

        } catch (Exception e) {
            // Manejo de errores específicos
            String mensajeError = e.getMessage();
            if (mensajeError.contains("Duplicate entry")) {
                throw new RuntimeException("Credenciales duplicadas");
            } else if (mensajeError.contains("cannot be null")) {
                throw new RuntimeException("Faltan campos requeridos");
            }

            throw new RuntimeException(mensajeError);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, String> userData) {
        Map<String, Object> response = new HashMap<>();

        try {
            String username = userData.get("username");
            String password = userData.get("password");

            // Validar username
            String errorUsername = validacion.validarUsername(username);
            if (errorUsername != null) {
                throw new RuntimeException(errorUsername);
            }

            // Validar password
            String errorPassword = validacion.validarPassword(password);
            if (errorPassword != null) {
                throw new RuntimeException(errorPassword);
            }

            // Verificar si ya existe el usuario
            if (userService.findByUsername(username.trim()) != null) {
                throw new RuntimeException("Username ya existente");
            }

            // Crear nuevo usuario
            Usuario nuevoUsuario = new Usuario(username.trim(), password);
            Usuario usuarioGuardado = userService.save(nuevoUsuario);

            response.put("success", true);
            response.put("mensaje", "Usuario registrado exitosamente");
            response.put("usuario", Map.of(
                    "id", usuarioGuardado.getId(),
                    "username", usuarioGuardado.getUsername()
            ));

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            // Manejo de errores específicos de base de datos
            String mensajeError = e.getMessage();
            if (mensajeError.contains("email") && mensajeError.contains("default value")) {
                throw new RuntimeException("Error en campo email requerido");
            } else if (mensajeError.contains("Duplicate entry")) {
                throw new RuntimeException("El usuario ya existe");
            } else if (mensajeError.contains("cannot be null")) {
                throw new RuntimeException("Faltan campos requeridos");
            }

            throw new RuntimeException(mensajeError);
        }
    }

    @GetMapping("/profile/{userId}")
    public ResponseEntity<Map<String, Object>> getUserProfile(@PathVariable Integer userId) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Validar que el ID sea válido
            if (userId == null || userId <= 0) {
                throw new RuntimeException("ID de usuario invalido");
            }

            Optional<Usuario> optionalUsuario = userService.findById(userId);
            if (optionalUsuario.isPresent()) {
                Usuario usuario = optionalUsuario.get();
                response.put("success", true);
                response.put("usuario", Map.of(
                        "id", usuario.getId(),
                        "username", usuario.getUsername(),
                        "partidasJugadas", usuario.getPartidasJugadas(),
                        "partidasGanadas", usuario.getPartidasGanadas(),
                        "porcentajeVictoria", usuario.getPartidasJugadas() > 0 ?
                                (double) usuario.getPartidasGanadas() / usuario.getPartidasJugadas() * 100 : 0
                ));
                return ResponseEntity.ok(response);
            } else {
                throw new RuntimeException("Usuario no encontrado");
            }

        } catch (Exception e) {
            // Manejo de errores específicos de base de datos
            String mensajeError = e.getMessage();
            if (mensajeError.contains("Duplicate entry")) {
                throw new RuntimeException("Usuario duplicado");
            } else if (mensajeError.contains("cannot be null")) {
                throw new RuntimeException("Faltan campos requeridos");
            }

            throw new RuntimeException(mensajeError);
        }
    }
}