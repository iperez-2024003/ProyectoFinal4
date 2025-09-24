package com.iversonperez.ProyectoFinalAhorcado.service;

import org.springframework.stereotype.Component;
import java.util.regex.Pattern;

@Component
public class Validacion {


    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{3,20}$");


    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d).{6,}$");


    public String validarUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return "El username es requerido";
        }

        username = username.trim();

        if (username.length() < 3) {
            return "El username debe tener al menos 3 caracteres";
        }

        if (username.length() > 20) {
            return "El username no puede tener mas de 20 caracteres";
        }

        if (!USERNAME_PATTERN.matcher(username).matches()) {
            return "El username solo puede contener letras, numeros y guiones bajos";
        }

        return null; // Es válido
    }

    public String validarPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            return "La contraseña es requerida";
        }

        if (password.length() < 6) {
            return "La contraseña debe tener al menos 6 caracteres";
        }

        if (password.length() > 50) {
            return "La contraseña no puede tener mas de 50 caracteres";
        }

        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            return "La contraseña debe contener al menos una letra y un numero";
        }

        return null; // Es válida
    }




    public String validarCredencialesLogin(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            return "El username es requerido";
        }

        if (password == null || password.trim().isEmpty()) {
            return "La contraseña es requerida";
        }

        return null;
    }
}