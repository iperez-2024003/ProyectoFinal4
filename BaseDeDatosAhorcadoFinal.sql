drop database if exists DB_ahorcado;
Create database DB_ahorcado;
use DB_ahorcado;

-- Tabla de palabras
create table palabras (
    id int auto_increment primary key,
    palabra varchar(50) not null,
    pista1 varchar(255) not null,
    pista2 varchar(255) not null,
    pista3 varchar(255) not null
);

-- Tabla de usuarios
create table usuarios (
    id int auto_increment primary key,
    username varchar(50) unique not null,
    email varchar(100) unique not null,
    password varchar(255) not null,
    partidas_jugadas int default 0,
    partidas_ganadas int default 0,
    created_at timestamp default current_timestamp
);

-- Tabla de partidas
create table partidas (
    id int auto_increment primary key,
    usuario_id int,
    palabra_id int,
    palabra varchar(50) not null,
    letras_adivinadas varchar(255) default '',
    letras_incorrectas varchar(255) default '',
    intentos_fallidos int default 0,
    max_intentos int default 6,
    estado enum('EN_PROGRESO', 'GANADA', 'PERDIDA') default 'EN_PROGRESO',
    pistas_usadas int default 0,
    created_at timestamp default current_timestamp,
    completed_at timestamp null,
    foreign key (usuario_id) references usuarios(id),
    foreign key (palabra_id) references palabras(id)
);

-- Stored Procedures
DELIMITER $$

-- SP: Insertar palabra
CREATE PROCEDURE sp_insertar_palabra(
    IN p_palabra VARCHAR(50),
    IN p_pista1 VARCHAR(255),
    IN p_pista2 VARCHAR(255),
    IN p_pista3 VARCHAR(255)
)
BEGIN
    INSERT INTO palabras (palabra, pista1, pista2, pista3)
    VALUES (p_palabra, p_pista1, p_pista2, p_pista3);
END $$

-- SP: Obtener todas las palabras
CREATE PROCEDURE sp_obtener_palabras()
BEGIN
    SELECT id, palabra, pista1, pista2, pista3 FROM palabras;
END $$

-- SP: Obtener palabra aleatoria
CREATE PROCEDURE sp_obtener_palabra_aleatoria()
BEGIN
    SELECT id, palabra, pista1, pista2, pista3 
    FROM palabras 
    ORDER BY RAND() 
    LIMIT 1;
END $$

-- SP: Crear nueva partida
CREATE PROCEDURE sp_crear_partida(
    IN p_usuario_id INT,
    IN p_palabra_id INT,
    IN p_palabra VARCHAR(50),
    OUT p_partida_id INT
)
BEGIN
    INSERT INTO partidas (usuario_id, palabra_id, palabra, estado)
    VALUES (p_usuario_id, p_palabra_id, p_palabra, 'EN_PROGRESO');
    SET p_partida_id = LAST_INSERT_ID();
END $$

-- SP: Actualizar partida
CREATE PROCEDURE sp_actualizar_partida(
    IN p_partida_id INT,
    IN p_letras_adivinadas VARCHAR(255),
    IN p_letras_incorrectas VARCHAR(255),
    IN p_intentos_fallidos INT,
    IN p_estado VARCHAR(20),
    IN p_pistas_usadas INT
)
BEGIN
    UPDATE partidas 
    SET letras_adivinadas = p_letras_adivinadas,
        letras_incorrectas = p_letras_incorrectas,
        intentos_fallidos = p_intentos_fallidos,
        estado = p_estado,
        pistas_usadas = p_pistas_usadas,
        completed_at = CASE WHEN p_estado != 'EN_PROGRESO' THEN NOW() ELSE completed_at END
    WHERE id = p_partida_id;
END $$

-- SP: Validar usuario
CREATE PROCEDURE sp_validar_usuario(
    IN p_username VARCHAR(50),
    IN p_password VARCHAR(255),
    OUT p_usuario_id INT
)
BEGIN
    SELECT id INTO p_usuario_id
    FROM usuarios 
    WHERE username = p_username AND password = p_password;
END $$

DELIMITER ;

-- Trigger para actualizar estadísticas
DELIMITER $$
CREATE TRIGGER tr_actualizar_estadisticas 
AFTER UPDATE ON partidas
FOR EACH ROW
BEGIN
    IF NEW.estado != OLD.estado AND NEW.estado IN ('GANADA', 'PERDIDA') THEN
        UPDATE usuarios 
        SET partidas_jugadas = partidas_jugadas + 1,
            partidas_ganadas = partidas_ganadas + CASE WHEN NEW.estado = 'GANADA' THEN 1 ELSE 0 END
        WHERE id = NEW.usuario_id;
    END IF;
END $$
DELIMITER ;

-- Función para calcular porcentaje de victoria
DELIMITER $$
CREATE FUNCTION fn_porcentaje_victoria(p_usuario_id INT)
RETURNS DECIMAL(5,2)
READS SQL DATA
DETERMINISTIC
BEGIN
    DECLARE v_jugadas INT DEFAULT 0;
    DECLARE v_ganadas INT DEFAULT 0;
    DECLARE v_porcentaje DECIMAL(5,2) DEFAULT 0.00;
    
    SELECT partidas_jugadas, partidas_ganadas 
    INTO v_jugadas, v_ganadas
    FROM usuarios 
    WHERE id = p_usuario_id;
    
    IF v_jugadas > 0 THEN
        SET v_porcentaje = (v_ganadas / v_jugadas) * 100;
    END IF;
    
    RETURN v_porcentaje;
END $$
DELIMITER ;

-- Insertar datos de prueba
-- Usuarios de prueba
INSERT INTO usuarios (username, email, password) VALUES 
('admin', 'admin@test.com', 'admin123'),
('player1', 'player1@test.com', 'pass123');

-- Palabras (usando CALL para los stored procedures)
CALL sp_insertar_palabra('JAVASCRIPT', 'Lenguaje de programación muy popular.', 'Se ejecuta principalmente en navegadores web.', 'Fundamental para crear páginas web interactivas.');
CALL sp_insertar_palabra('PROGRAMACION', 'Arte de crear software y aplicaciones.', 'Requiere lógica, creatividad y paciencia.', 'Se utilizan lenguajes específicos para comunicarse con la máquina.');
CALL sp_insertar_palabra('COMPUTADORA', 'Máquina electrónica inteligente.', 'Capaz de procesar grandes cantidades de información.', 'Combina componentes físicos (hardware) y programas (software).');
CALL sp_insertar_palabra('ALGORITMO', 'Secuencia ordenada de pasos o instrucciones.', 'Diseñado para resolver problemas específicos.', 'Constituye la base fundamental de toda programación.');
CALL sp_insertar_palabra('DESARROLLO', 'Proceso completo de crear aplicaciones informáticas.', 'Involucra análisis, diseño y planificación detallada.', 'Requiere pruebas exhaustivas y mantenimiento continuo.');
CALL sp_insertar_palabra('TECNOLOGIA', 'Aplicación práctica del conocimiento científico.', 'Su objetivo es mejorar la calidad de vida humana.', 'Se encuentra en constante evolución e innovación.');
CALL sp_insertar_palabra('INGENIERIA', 'Aplicación de ciencias exactas y matemáticas.', 'Se enfoca en diseñar y construir soluciones prácticas.', 'Combina conocimiento técnico con creatividad innovadora.');
CALL sp_insertar_palabra('CREATIVIDAD', 'Capacidad humana de generar ideas nuevas y originales.', 'Es fundamental en todas las expresiones artísticas.', 'Requiere imaginación, intuición y pensamiento divergente.');

select * from Usuarios;