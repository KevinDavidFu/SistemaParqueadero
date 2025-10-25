package com.example.parking.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.parking.model.Tarifa;
import com.example.parking.service.ServicioTarifa;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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

        try (PrintWriter out = response.getWriter()) {
            List<Tarifa> lista = servicioTarifa.listarTarifas();
            if (lista == null) lista = new ArrayList<>();

            out.println("<html><head><title>Lista de Tarifas</title></head><body>");
            out.println("<h2>Tarifas registradas</h2>");
            out.println("<table border='1'><tr><th>ID</th><th>Tipo Vehículo</th><th>Precio por Hora</th></tr>");
            for (Tarifa t : lista) {
                out.println("<tr><td>" + t.getId() + "</td><td>" + t.getTipo() +
                        "</td><td>$" + t.getPrecioPorHora() + "</td></tr>");
            }
            out.println("</table>");
            out.println("<a href='index.html'>Volver al inicio</a>");
            out.println("</body></html>");
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try (PrintWriter out = response.getWriter()) {
                out.println("<h3>Error al obtener tarifas: " + e.getMessage() + "</h3>");
                out.println("<a href='index.html'>Volver al inicio</a>");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String tipo = request.getParameter("tipo");
        String precioStr = request.getParameter("precioPorHora");

        if (tipo == null || tipo.trim().isEmpty()) tipo = "";
        if (precioStr == null || precioStr.trim().isEmpty()) precioStr = "0";

        double precioPorHora = Double.parseDouble(precioStr.trim());

        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            servicioTarifa.agregarTarifa(tipo, precioPorHora);
            out.println("<h3>Tarifa registrada correctamente.</h3>");
            out.println("<a href='tarifas'>Volver a la lista</a>");
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try (PrintWriter out = response.getWriter()) {
                out.println("<h3>Error al registrar tarifa: " + e.getMessage() + "</h3>");
                out.println("<a href='agregarTarifa.html'>Volver</a>");
            }
        }
    }
}
