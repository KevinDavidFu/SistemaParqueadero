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
        List<Vehiculo> lista = servicioVehiculo.listarVehiculos();

        try (PrintWriter out = response.getWriter()) {
            out.println("<html><head><title>Lista de Vehículos</title></head><body>");
            out.println("<h2>Vehículos registrados</h2>");
            out.println("<table border='1'><tr><th>ID</th><th>Placa</th><th>Tipo</th><th>Hora Entrada</th></tr>");
            for (Vehiculo v : lista) {
                out.println("<tr><td>" + v.getIdVehiculo() + "</td><td>" + v.getPlaca() +
                        "</td><td>" + v.getTipo() + "</td><td>" + v.getHoraEntrada() + "</td></tr>");
            }
            out.println("</table>");
            out.println("</body></html>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String placa = request.getParameter("placa");
        String tipo = request.getParameter("tipo");
        String horaEntrada = request.getParameter("horaEntrada");

        Vehiculo nuevo = new Vehiculo();
        nuevo.setPlaca(placa);
        nuevo.setTipo(tipo);
        nuevo.setHoraEntrada(horaEntrada);

        boolean insertado = servicioVehiculo.registrarVehiculo(nuevo);

        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            if (insertado) {
                out.println("<h3>Vehículo registrado correctamente.</h3>");
            } else {
                out.println("<h3>Error al registrar el vehículo.</h3>");
            }
            out.println("<a href='vehiculos'>Volver a la lista</a>");
        }
    }
}
