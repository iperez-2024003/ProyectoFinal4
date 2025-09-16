package Modelo;

import Config.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PalabraDAO {

    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    public boolean insertarPalabra(Palabra p) {
        String sql = "INSERT INTO palabras (palabra, pista1, pista2, pista3) VALUES (?, ?, ?, ?)";
        try {
            con = cn.Conexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, p.getPalabra());
            ps.setString(2, p.getPista1());
            ps.setString(3, p.getPista2());
            ps.setString(4, p.getPista3());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al insertar palabra: " + e.getMessage());
            return false;
        }
    }

    public List<Palabra> obtenerPalabras() {
        List<Palabra> lista = new ArrayList<>();
        String sql = "SELECT id, palabra, pista1, pista2, pista3 FROM palabras";
        try {
            con = cn.Conexion();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Palabra p = new Palabra();
                p.setId(rs.getInt("id"));
                p.setPalabra(rs.getString("palabra"));
                p.setPista1(rs.getString("pista1"));
                p.setPista2(rs.getString("pista2"));
                p.setPista3(rs.getString("pista3"));
                lista.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener palabras: " + e.getMessage());
        }
        return lista;
    }

    public Palabra obtenerPalabraAleatoria() {
        Palabra p = null;
        String sql = "SELECT id, palabra, pista1, pista2, pista3 FROM palabras ORDER BY RAND() LIMIT 1";
        try {
            con = cn.Conexion();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                p = new Palabra();
                p.setId(rs.getInt("id"));
                p.setPalabra(rs.getString("palabra"));
                p.setPista1(rs.getString("pista1"));
                p.setPista2(rs.getString("pista2"));
                p.setPista3(rs.getString("pista3"));
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener palabra aleatoria: " + e.getMessage());
        }
        return p;
    }
}