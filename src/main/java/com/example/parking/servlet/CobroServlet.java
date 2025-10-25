package com.example.parking.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import com.example.parking.model.Vehiculo;
import com.example.parking.service.ServicioTarifa;
import com.example.parking.service.ServicioVehiculo;
import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/cobro")
public class CobroServlet extends HttpServlet {

    private ServicioVehiculo servicioVehiculo;
    private ServicioTarifa servicioTarifa;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        servicioVehiculo = new ServicioVehiculo();
        servicioTarifa = new ServicioTarifa();
        gson = new Gson();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");
        String placa = request.getParameter("placa");
        Map<String, Object> result = new HashMap<>();
        
        try (PrintWriter out = response.getWriter()) {
            if (placa == null || placa.trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "La placa es requerida");
                out.print(gson.toJson(result));
                return;
            }

            Vehiculo vehiculo = servicioVehiculo.buscarVehiculo(placa);

            if (vehiculo == null) {
                result.put("success", false);
                result.put("message", "Vehículo no encontrado");
                out.print(gson.toJson(result));
                return;
            }

            if (!vehiculo.isActivo()) {
                result.put("success", false);
                result.put("message", "Este vehículo ya tiene registrada su salida");
                out.print(gson.toJson(result));
                return;
            }

            double precioPorHora = servicioTarifa.obtenerPrecioPorTipo(vehiculo.getTipo());
            
            if (precioPorHora == 0.0) {
                result.put("success", false);
                result.put("message", "No se encontró tarifa para: " + vehiculo.getTipo());
                out.print(gson.toJson(result));
                return;
            }

            Duration duracion = Duration.between(vehiculo.getIngreso(), LocalDateTime.now());
            double horas = duracion.toMinutes() / 60.0;
            if (horas < 1.0) horas = 1.0;
            
            double totalPagar = Math.round(horas * precioPorHora * 100.0) / 100.0;

            boolean actualizado = servicioVehiculo.registrarSalida(placa, totalPagar);

            if (actualizado) {
                result.put("success", true);
                result.put("message", "Cobro realizado exitosamente");
                result.put("total", totalPagar);
                result.put("horas", Math.round(horas * 100.0) / 100.0);
                result.put("precioPorHora", precioPorHora);
                result.put("vehiculo", vehiculo.getPlaca());
            } else {
                result.put("success", false);
                result.put("message", "Error al actualizar el registro");
            }
            
            out.print(gson.toJson(result));

        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.put("success", false);
            result.put("message", "Error: " + e.getMessage());
            response.getWriter().print(gson.toJson(result));
        }
    }
}