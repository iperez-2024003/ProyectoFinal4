package com.iversonperez.ProyectoFinalAhorcado.controller;

import com.iversonperez.ProyectoFinalAhorcado.model.Palabra;
import com.iversonperez.ProyectoFinalAhorcado.service.PalabraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/palabras")
@CrossOrigin(origins = "*")
public class PalabraController {

    @Autowired
    private PalabraService palabraService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> agregar(@RequestBody Palabra palabra) {
        Map<String, Object> response = new HashMap<>();
        try {
            palabraService.agregarPalabra(palabra);
            response.put("success", true);
            response.put("mensaje", "Palabra agregada exitosamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("mensaje", "Error al agregar: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping
    public List<Palabra> listar() {
        return palabraService.listarPalabras();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizar(@PathVariable Integer id, @RequestBody Palabra palabra) {
        Map<String, Object> response = new HashMap<>();
        try {
            Palabra updated = palabraService.actualizarPalabra(id, palabra);
            response.put("success", true);
            response.put("palabra", updated);
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
            palabraService.eliminarPalabra(id);
            response.put("success", true);
            response.put("mensaje", "Palabra eliminada");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("mensaje", "Error al eliminar: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}