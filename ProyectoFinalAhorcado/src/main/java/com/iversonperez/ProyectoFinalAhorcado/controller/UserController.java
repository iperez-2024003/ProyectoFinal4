package com.iversonperez.ProyectoFinalAhorcado.controller;

import com.iversonperez.ProyectoFinalAhorcado.model.Usuario;
import com.iversonperez.ProyectoFinalAhorcado.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> agregar(@RequestBody Usuario usuario) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (userService.findByUsername(usuario.getUsername()) != null) {
                response.put("success", false);
                response.put("mensaje", "Username ya existe");
                return ResponseEntity.badRequest().body(response);
            }
            Usuario saved = userService.save(usuario);
            response.put("success", true);
            response.put("usuario", saved);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("mensaje", "Error al agregar: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping
    public List<Usuario> listar() {
        return userService.findAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizar(@PathVariable Integer id, @RequestBody Usuario usuario) {
        Map<String, Object> response = new HashMap<>();
        try {
            Usuario updated = userService.actualizarUsuario(id, usuario);
            response.put("success", true);
            response.put("usuario", updated);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("mensaje", "Error al actualizar: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();
        try {
            userService.eliminarUsuario(id);
            response.put("success", true);
            response.put("mensaje", "Usuario eliminado");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("mensaje", "Error al eliminar: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}