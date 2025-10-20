package com.example.parking.servlet;

import com.example.parking.model.Tarifa;
import com.example.parking.service.ServicioTarifa;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/tarifas")
public class TarifaServlet extends HttpServlet {

    private ServicioTarifa servicioTarifa;

    @Override
    public void init() throws ServletException {
        servicioTarifa = new ServicioTarifa();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        List<Tarifa> lista = servicioTarifa.listarTarifas();

        try (PrintWriter out = response.getWriter()) {
            out.println("<html><head><title>Lista de Tarifas</title></head><body>");
            out.println("<h2>Tarifas registradas</h2>");
            out.println("<table border='1'><tr><th>ID</th><th>Tipo Vehículo</th><th>Precio Hora</th></tr>");
            for (Tarifa t : lista) {
                out.println("<tr><td>" + t.getIdTarifa() + "</td><td>" + t.getTipoVehiculo() +
                        "</td><td>" + t.getPrecioHora() + "</td></tr>");
            }
            out.println("</table>");
            out.println("</body></html>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String tipoVehiculo = request.getParameter("tipoVehiculo");
        double precioHora = Double.parseDouble(request.getParameter("precioHora"));

        Tarifa nueva = new Tarifa();
        nueva.setTipoVehiculo(tipoVehiculo);
        nueva.setPrecioHora(precioHora);

        boolean insertada = servicioTarifa.registrarTarifa(nueva);

        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            if (insertada) {
                out.println("<h3>Tarifa registrada correctamente.</h3>");
            } else {
                out.println("<h3>Error al registrar la tarifa.</h3>");
            }
            out.println("<a href='tarifas'>Volver a la lista</a>");
        }
    }
}
