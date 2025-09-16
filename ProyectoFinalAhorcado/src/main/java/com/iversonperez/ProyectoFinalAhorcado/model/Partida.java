package com.iversonperez.ProyectoFinalAhorcado.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "partidas")
public class Partida {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "usuario_id")
    private Integer usuarioId;

    @Column(name = "palabra_id")
    private Integer palabraId;

    @Column(nullable = false, length = 50)
    private String palabra;

    @Column(name = "letras_adivinadas")
    private String letrasAdivinadas = "";

    @Column(name = "letras_incorrectas")
    private String letrasIncorrectas = "";

    @Column(name = "intentos_fallidos")
    private Integer intentosFallidos = 0;

    @Column(name = "max_intentos")
    private Integer maxIntentos = 6;

    @Enumerated(EnumType.STRING)
    private EstadoPartida estado = EstadoPartida.EN_PROGRESO;

    @Column(name = "pistas_usadas")
    private Integer pistasUsadas = 0;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    // Constructores
    public Partida() {}

    public Partida(Integer usuarioId, Integer palabraId, String palabra) {
        this.usuarioId = usuarioId;
        this.palabraId = palabraId;
        this.palabra = palabra;
    }

    // Getters y Setters completos...
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Integer usuarioId) { this.usuarioId = usuarioId; }

    public Integer getPalabraId() { return palabraId; }
    public void setPalabraId(Integer palabraId) { this.palabraId = palabraId; }

    public String getPalabra() { return palabra; }
    public void setPalabra(String palabra) { this.palabra = palabra; }

    public String getLetrasAdivinadas() { return letrasAdivinadas; }
    public void setLetrasAdivinadas(String letrasAdivinadas) { this.letrasAdivinadas = letrasAdivinadas; }

    public String getLetrasIncorrectas() { return letrasIncorrectas; }
    public void setLetrasIncorrectas(String letrasIncorrectas) { this.letrasIncorrectas = letrasIncorrectas; }

    public Integer getIntentosFallidos() { return intentosFallidos; }
    public void setIntentosFallidos(Integer intentosFallidos) { this.intentosFallidos = intentosFallidos; }

    public Integer getMaxIntentos() { return maxIntentos; }
    public void setMaxIntentos(Integer maxIntentos) { this.maxIntentos = maxIntentos; }

    public EstadoPartida getEstado() { return estado; }
    public void setEstado(EstadoPartida estado) { this.estado = estado; }

    public Integer getPistasUsadas() { return pistasUsadas; }
    public void setPistasUsadas(Integer pistasUsadas) { this.pistasUsadas = pistasUsadas; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
}

