package com.example.parking.servlet;

import com.example.parking.model.Vehiculo;
import com.example.parking.service.ServicioTarifa;
import com.example.parking.service.ServicioVehiculo;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;

@WebServlet("/cobro")
public class CobroServlet extends HttpServlet {

    private ServicioVehiculo servicioVehiculo;
    private ServicioTarifa servicioTarifa;

    @Override
    public void init() throws ServletException {
        servicioVehiculo = new ServicioVehiculo();
        servicioTarifa = new ServicioTarifa();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String placa = request.getParameter("placa");
        response.setContentType("text/html;charset=UTF-8");
        
        try (PrintWriter out = response.getWriter()) {
            Vehiculo vehiculo = servicioVehiculo.buscarVehiculo(placa);

            if (vehiculo == null) {
                out.println("<h3>Vehículo no encontrado.</h3>");
                out.println("<a href='vehiculos'>Volver a la lista</a>");
                return;
            }

            if (!vehiculo.isActivo()) {
                out.println("<h3>Este vehículo ya tiene registrada su salida.</h3>");
                out.println("<a href='vehiculos'>Volver a la lista</a>");
                return;
            }

            // Obtener precio por tipo
            double precioPorHora = servicioTarifa.obtenerPrecioPorTipo(vehiculo.getTipo());
            
            if (precioPorHora == 0.0) {
                out.println("<h3>No se encontró tarifa para el tipo: " + vehiculo.getTipo() + "</h3>");
                out.println("<a href='vehiculos'>Volver a la lista</a>");
                return;
            }

            // Calcular tiempo de parqueo en horas
            Duration duracion = Duration.between(vehiculo.getIngreso(), LocalDateTime.now());
            double horas = duracion.toMinutes() / 60.0;
            
            // Redondear a mínimo 1 hora
            if (horas < 1.0) {
                horas = 1.0;
            }
            
            double totalPagar = Math.round(horas * precioPorHora * 100.0) / 100.0;

            // Registrar salida y pago
            boolean actualizado = servicioVehiculo.registrarSalida(placa, totalPagar);

            if (actualizado) {
                out.println("<html><head><title>Cobro Realizado</title></head><body>");
                out.println("<h2>Cobro calculado correctamente</h2>");
                out.println("<p><strong>Vehículo:</strong> " + vehiculo.getPlaca() + "</p>");
                out.println("<p><strong>Modelo:</strong> " + vehiculo.getModelo() + "</p>");
                out.println("<p><strong>Tipo:</strong> " + vehiculo.getTipo() + "</p>");
                out.println("<p><strong>Hora de ingreso:</strong> " + vehiculo.getIngreso() + "</p>");
                out.println("<p><strong>Hora de salida:</strong> " + LocalDateTime.now() + "</p>");
                out.println("<p><strong>Tiempo parqueo:</strong> " + String.format("%.2f", horas) + " horas</p>");
                out.println("<p><strong>Precio por hora:</strong> $" + precioPorHora + "</p>");
                out.println("<h3>Total a pagar: $" + totalPagar + "</h3>");
                out.println("<a href='vehiculos'>Volver a la lista</a>");
                out.println("</body></html>");
            } else {
                out.println("<h3>Error al actualizar el registro de salida.</h3>");
                out.println("<a href='vehiculos'>Volver a la lista</a>");
            }

        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try (PrintWriter out = response.getWriter()) {
                out.println("<h3>Error al procesar cobro: " + e.getMessage() + "</h3>");
                out.println("<a href='vehiculos'>Volver</a>");
            }
        }
    }
}