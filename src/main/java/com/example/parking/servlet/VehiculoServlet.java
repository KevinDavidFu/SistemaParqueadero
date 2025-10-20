package com.example.parking.servlet;

import com.example.parking.model.Vehiculo;
import com.example.parking.service.ServicioVehiculo;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/vehiculos")
public class VehiculoServlet extends HttpServlet {

    private ServicioVehiculo servicioVehiculo;

    @Override
    public void init() throws ServletException {
        servicioVehiculo = new ServicioVehiculo();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        
        try (PrintWriter out = response.getWriter()) {
            List<Vehiculo> lista = servicioVehiculo.listarVehiculos();
            
            out.println("<html><head><title>Lista de Vehículos</title></head><body>");
            out.println("<h2>Vehículos registrados</h2>");
            out.println("<table border='1'><tr><th>ID</th><th>Placa</th><th>Modelo</th><th>Tipo</th><th>Ingreso</th><th>Activo</th></tr>");
            for (Vehiculo v : lista) {
                out.println("<tr><td>" + v.getId() + "</td><td>" + v.getPlaca() +
                        "</td><td>" + v.getModelo() + "</td><td>" + v.getTipo() + 
                        "</td><td>" + v.getIngreso() + "</td><td>" + v.isActivo() + "</td></tr>");
            }
            out.println("</table>");
            out.println("<a href='index.html'>Volver al inicio</a>");
            out.println("</body></html>");
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try (PrintWriter out = response.getWriter()) {
                out.println("<h3>Error al obtener vehículos: " + e.getMessage() + "</h3>");
                out.println("<a href='index.html'>Volver al inicio</a>");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String placa = request.getParameter("placa");
        String modelo = request.getParameter("modelo");
        String tipo = request.getParameter("tipo");

        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            boolean insertado = servicioVehiculo.registrarVehiculo(placa, modelo, tipo);
            
            if (insertado) {
                out.println("<h3>Vehículo registrado correctamente.</h3>");
            } else {
                out.println("<h3>Error al registrar el vehículo.</h3>");
            }
            out.println("<a href='vehiculos'>Volver a la lista</a>");
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try (PrintWriter out = response.getWriter()) {
                out.println("<h3>Error al registrar vehículo: " + e.getMessage() + "</h3>");
                out.println("<a href='agregarVehiculo.html'>Volver</a>");
            }
        }
    }
}