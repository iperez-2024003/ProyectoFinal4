package com.iversonperez.ProyectoFinalAhorcado.service;

import com.iversonperez.ProyectoFinalAhorcado.model.EstadoPartida;
import com.iversonperez.ProyectoFinalAhorcado.model.Partida;
import com.iversonperez.ProyectoFinalAhorcado.model.Palabra;
import com.iversonperez.ProyectoFinalAhorcado.model.Usuario;
import com.iversonperez.ProyectoFinalAhorcado.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.dao.DataAccessException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class UserService implements UserServiceImplements {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Usuario save(Usuario usuario) {
        return userRepository.save(usuario);
    }

    @Override
    public List<Usuario> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Usuario findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public Usuario validarUsuario(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(user -> user.getPassword().equals(password))
                .orElse(null);
    }

    @Override
    public Palabra obtenerPalabraAleatoria() {
        try {
            String sql = "CALL sp_obtener_palabra_aleatoria()";
            Map<String, Object> result = jdbcTemplate.queryForMap(sql);

            Palabra palabra = new Palabra();
            palabra.setId((Integer) result.get("id"));
            palabra.setPalabra((String) result.get("palabra"));
            palabra.setPista1((String) result.get("pista1"));
            palabra.setPista2((String) result.get("pista2"));
            palabra.setPista3((String) result.get("pista3"));

            return palabra;
        } catch (DataAccessException e) {
            throw new RuntimeException("Error al obtener palabra: " + e.getMessage());
        }
    }

    @Override
    public String obtenerPista(Integer partidaId, Integer numeroPista) {
        try {
            Partida partida = obtenerPartidaPorId(partidaId);
            Palabra palabra = obtenerPalabraPorId(partida.getPalabraId());

            if (numeroPista < 1 || numeroPista > 3) {
                throw new RuntimeException("Número de pista inválido");
            }

            if (partida.getPistasUsadas() >= numeroPista) {
                // Ya usó esta pista, solo devolverla
                return switch (numeroPista) {
                    case 1 -> palabra.getPista1();
                    case 2 -> palabra.getPista2();
                    case 3 -> palabra.getPista3();
                    default -> throw new RuntimeException("Pista no válida");
                };
            }

            // Actualizar contador de pistas usadas
            String sql = "CALL sp_actualizar_partida(?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql, partidaId, partida.getLetrasAdivinadas(),
                    partida.getLetrasIncorrectas(), partida.getIntentosFallidos(),
                    partida.getEstado().name(), numeroPista);

            return switch (numeroPista) {
                case 1 -> palabra.getPista1();
                case 2 -> palabra.getPista2();
                case 3 -> palabra.getPista3();
                default -> throw new RuntimeException("Pista no válida");
            };

        } catch (DataAccessException e) {
            throw new RuntimeException("Error al obtener pista: " + e.getMessage());
        }
    }

    @Override
    public Partida crearNuevaPartida(Integer usuarioId) {
        try {
            // Obtener palabra aleatoria
            Palabra palabra = obtenerPalabraAleatoria();

            // Crear nueva partida usando stored procedure con parámetro OUT
            String sql = "CALL sp_crear_partida(?, ?, ?, @partida_id)";
            jdbcTemplate.update(sql, usuarioId, palabra.getId(), palabra.getPalabra());

            // Obtener el ID de la partida creada
            String getIdSql = "SELECT @partida_id as partida_id";
            Map<String, Object> result = jdbcTemplate.queryForMap(getIdSql);
            Integer partidaId = (Integer) result.get("partida_id");

            return obtenerPartidaPorId(partidaId);

        } catch (DataAccessException e) {
            throw new RuntimeException("Error al crear nueva partida: " + e.getMessage());
        }
    }

    @Override
    public Partida adivinarLetra(Integer partidaId, String letra) {
        try {
            Partida partida = obtenerPartidaPorId(partidaId);

            if (!partida.getEstado().equals(EstadoPartida.EN_PROGRESO)) {
                throw new RuntimeException("La partida ya ha terminado");
            }

            String palabraActual = partida.getPalabra().toUpperCase();
            String letraUpper = letra.toUpperCase();

            // Verificar si la letra ya fue adivinada
            if (partida.getLetrasAdivinadas().contains(letraUpper) ||
                    partida.getLetrasIncorrectas().contains(letraUpper)) {
                throw new RuntimeException("Esta letra ya fue utilizada");
            }

            String letrasAdivinadas = partida.getLetrasAdivinadas();
            String letrasIncorrectas = partida.getLetrasIncorrectas();
            Integer intentosFallidos = partida.getIntentosFallidos();
            EstadoPartida estado = partida.getEstado();

            if (palabraActual.contains(letraUpper)) {
                // Letra correcta
                letrasAdivinadas += letraUpper;

                // Verificar si ganó
                boolean todasLasLetrasAdivinadas = true;
                for (char c : palabraActual.toCharArray()) {
                    if (!letrasAdivinadas.contains(String.valueOf(c))) {
                        todasLasLetrasAdivinadas = false;
                        break;
                    }
                }

                if (todasLasLetrasAdivinadas) {
                    estado = EstadoPartida.GANADA;
                    // Actualizar estadísticas del usuario
                    actualizarEstadisticasUsuario(partida.getUsuarioId(), true);
                }
            } else {
                // Letra incorrecta
                letrasIncorrectas += letraUpper;
                intentosFallidos++;

                if (intentosFallidos >= partida.getMaxIntentos()) {
                    estado = EstadoPartida.PERDIDA;
                    // Actualizar estadísticas del usuario
                    actualizarEstadisticasUsuario(partida.getUsuarioId(), false);
                }
            }

            // Actualizar partida
            String sql = "CALL sp_actualizar_partida(?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql, partidaId, letrasAdivinadas, letrasIncorrectas,
                    intentosFallidos, estado.name(), partida.getPistasUsadas());

            return obtenerPartidaPorId(partidaId);

        } catch (DataAccessException e) {
            throw new RuntimeException("Error al procesar letra: " + e.getMessage());
        }
    }

    @Override
    public Partida obtenerPartidaPorId(Integer partidaId) {
        try {
            String sql = "SELECT * FROM partidas WHERE id = ?";
            Map<String, Object> result = jdbcTemplate.queryForMap(sql, partidaId);

            Partida partida = new Partida();
            partida.setId((Integer) result.get("id"));
            partida.setUsuarioId((Integer) result.get("usuario_id"));
            partida.setPalabraId((Integer) result.get("palabra_id"));
            partida.setPalabra((String) result.get("palabra"));
            partida.setLetrasAdivinadas((String) result.get("letras_adivinadas"));
            partida.setLetrasIncorrectas((String) result.get("letras_incorrectas"));
            partida.setIntentosFallidos((Integer) result.get("intentos_fallidos"));
            partida.setMaxIntentos((Integer) result.get("max_intentos"));
            partida.setEstado(EstadoPartida.valueOf((String) result.get("estado")));
            partida.setPistasUsadas((Integer) result.get("pistas_usadas"));

            return partida;

        } catch (DataAccessException e) {
            throw new RuntimeException("Error al obtener partida: " + e.getMessage());
        }
    }

    @Override
    public Palabra obtenerPalabraPorId(Integer palabraId) {
        try {
            String sql = "SELECT * FROM palabras WHERE id = ?";
            Map<String, Object> result = jdbcTemplate.queryForMap(sql, palabraId);

            Palabra palabra = new Palabra();
            palabra.setId((Integer) result.get("id"));
            palabra.setPalabra((String) result.get("palabra"));
            palabra.setPista1((String) result.get("pista1"));
            palabra.setPista2((String) result.get("pista2"));
            palabra.setPista3((String) result.get("pista3"));

            return palabra;

        } catch (DataAccessException e) {
            throw new RuntimeException("Error al obtener palabra: " + e.getMessage());
        }
    }

    private void actualizarEstadisticasUsuario(Integer usuarioId, boolean gano) {
        // No necesitamos este método porque tienes un TRIGGER en la base de datos
        // que automáticamente actualiza las estadísticas cuando cambia el estado de la partida
        // Comentado para evitar duplicación:

        // try {
        //     String sql = "CALL sp_actualizar_estadisticas_usuario(?, ?)";
        //     jdbcTemplate.update(sql, usuarioId, gano ? 1 : 0);
        // } catch (DataAccessException e) {
        //     System.err.println("Error actualizando estadísticas: " + e.getMessage());
        // }
    }
}