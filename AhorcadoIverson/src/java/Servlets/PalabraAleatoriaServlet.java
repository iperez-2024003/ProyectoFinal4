package Servlets;

import Modelo.Palabra;
import Modelo.PalabraDAO;
import Config.Conexion;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "PalabraAleatoriaServlet", urlPatterns = {"/PalabraAleatoria"})
public class PalabraAleatoriaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Integer usuarioId = (Integer) session.getAttribute("usuarioId");

        if (usuarioId == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        PalabraDAO dao = new PalabraDAO();
        Palabra palabra = dao.obtenerPalabraAleatoria();

        if (palabra != null) {
            // Crear una nueva partida
            Conexion cn = new Conexion();
            Connection con = null;
            CallableStatement cs = null;
            try {
                con = cn.Conexion();
                cs = con.prepareCall("{CALL sp_crear_partida(?, ?, ?, ?)}");
                cs.setInt(1, usuarioId);
                cs.setInt(2, palabra.getId());
                cs.setString(3, palabra.getPalabra());
                cs.registerOutParameter(4, Types.INTEGER);
                cs.execute();
                int partidaId = cs.getInt(4);

                // Guardar datos en la sesión
                session.setAttribute("partidaId", partidaId);
                session.setAttribute("palabra", palabra.getPalabra());
                session.setAttribute("pista1", palabra.getPista1());
                session.setAttribute("pista2", palabra.getPista2());
                session.setAttribute("pista3", palabra.getPista3());

                response.sendRedirect("Ahorcado.jsp");
            } catch (SQLException e) {
                response.getWriter().println("Error al crear partida: " + e.getMessage());
            } finally {
                try {
                    if (cs != null) cs.close();
                    if (con != null) con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            response.getWriter().println("No se pudo obtener una palabra de la base de datos.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}