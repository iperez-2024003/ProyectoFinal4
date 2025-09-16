package Controlador;

import Config.Conexion;
import java.io.IOException;
import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/Validar")
public class Validar extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String btnIngresar = request.getParameter("btnIngresar");

        if ("Ingresar".equalsIgnoreCase(btnIngresar)) {
            String usuario = request.getParameter("txtCorreo");
            String contra = request.getParameter("txtContrasena");

            Conexion cn = new Conexion();
            Connection con = null;
            CallableStatement cs = null;
            ResultSet rs = null;

            try {
                con = cn.Conexion();
                // Llamar al procedimiento almacenado sp_validar_usuario
                cs = con.prepareCall("{CALL sp_validar_usuario(?, ?, ?)}");
                cs.setString(1, usuario);
                cs.setString(2, contra);
                cs.registerOutParameter(3, java.sql.Types.INTEGER);
                cs.execute();

                int usuarioId = cs.getInt(3);

                if (usuarioId > 0) {
                    // Credenciales válidas, guardar el ID del usuario en la sesión
                    HttpSession session = request.getSession();
                    session.setAttribute("usuarioId", usuarioId);
                    session.setAttribute("username", usuario);
                    request.getRequestDispatcher("Ahorcado.jsp").forward(request, response);
                } else {
                    // Credenciales incorrectas
                    request.setAttribute("error", "Usuario o contraseña incorrectos");
                    request.getRequestDispatcher("index.jsp").forward(request, response);
                }
            } catch (SQLException e) {
                // Manejo de errores de base de datos
                request.setAttribute("error", "Error de base de datos: " + e.getMessage());
                request.getRequestDispatcher("index.jsp").forward(request, response);
            } finally {
                // Cerrar recursos
                try {
                    if (rs != null) rs.close();
                    if (cs != null) cs.close();
                    if (con != null) con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            response.sendRedirect("index.jsp");
        }
    }
}