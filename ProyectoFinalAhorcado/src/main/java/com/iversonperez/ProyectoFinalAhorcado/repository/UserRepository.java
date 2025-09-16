package com.iversonperez.ProyectoFinalAhorcado.repository;

import com.iversonperez.ProyectoFinalAhorcado.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByUsername(String username);

    Optional<Usuario> findByEmail(String email);

    @Query(value = "CALL sp_validar_usuario(:username, :password, @usuario_id); SELECT @usuario_id as usuario_id;", nativeQuery = true)
    Integer validarUsuario(@Param("username") String username, @Param("password") String password);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}