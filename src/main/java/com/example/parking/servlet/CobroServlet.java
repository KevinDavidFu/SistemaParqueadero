package com.example.parking.servlet;

import com.example.parking.model.Vehiculo;
import com.example.parking.model.Tarifa;
import com.example.parking.service.ServicioTarifa;
import com.example.parking.service.ServicioVehiculo;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;

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
        Vehiculo vehiculo = servicioVehiculo.buscarVehiculo(placa);

        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {

            if (vehiculo == null) {
                out.println("<h3>Vehículo no encontrado.</h3>");
                out.println("<a href='vehiculos'>Volver a la lista</a>");
                return;
            }

            Tarifa tarifa = servicioTarifa.obtenerTarifaPorId(vehiculo.getTipo().hashCode()); // Puedes adaptar según tu lógica de ID
            if (tarifa == null) {
                out.println("<h3>Tarifa no encontrada para el tipo de vehículo.</h3>");
                out.println("<a href='vehiculos'>Volver a la lista</a>");
                return;
            }

            // Calcular tiempo de parqueo en horas con fracción
            Duration duracion = Duration.between(vehiculo.getHoraEntrada(), java.time.LocalDateTime.now());
            double horas = duracion.toMinutes() / 60.0;
            double totalPagar = Math.round(horas * tarifa.getPrecioHora() * 100.0) / 100.0;

            // Registrar salida y pago
            boolean actualizado = servicioVehiculo.registrarSalida(placa, totalPagar);

            if (actualizado) {
                out.println("<h3>Cobro calculado correctamente.</h3>");
                out.println("<p>Vehículo: " + vehiculo.getPlaca() + "</p>");
                out.println("<p>Tiempo parqueo: " + String.format("%.2f", horas) + " horas</p>");
                out.println("<p>Total a pagar: $" + totalPagar + "</p>");
            } else {
                out.println("<h3>Error al actualizar el registro de salida.</h3>");
            }

            out.println("<a href='vehiculos'>Volver a la lista</a>");
        }
    }
}
