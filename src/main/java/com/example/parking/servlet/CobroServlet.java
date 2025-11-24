package com.example.parking.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import com.example.parking.service.VehiculoService;
import com.example.parking.service.VehiculoService.CobroResultDTO;
import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CobroServlet extends HttpServlet {

    private VehiculoService servicioVehiculo;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        servicioVehiculo = new VehiculoService();
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

            // Buscar vehículo por placa
            var vehiculoOpt = servicioVehiculo.buscarPorPlaca(placa);

            if (vehiculoOpt.isEmpty()) {
                result.put("success", false);
                result.put("message", "Vehículo no encontrado");
                out.print(gson.toJson(result));
                return;
            }

            var vehiculo = vehiculoOpt.get();

            
            if (!vehiculo.isActivo()) {
                result.put("success", false);
                result.put("message", "Este vehículo ya tiene registrada su salida");
                out.print(gson.toJson(result));
                return;
            }

            // Registrar salida
            CobroResultDTO cobro = servicioVehiculo.registrarSalida(placa);

            result.put("success", true);
            result.put("message", "Cobro realizado exitosamente");
            result.put("placa", cobro.getPlaca());
            result.put("horas", cobro.getHoras());
            result.put("precioPorHora", cobro.getPrecioPorHora());
            result.put("total", cobro.getTotal());
            result.put("ingreso", cobro.getIngreso().toString());
            result.put("salida", cobro.getSalida().toString());

            out.print(gson.toJson(result));

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            result.put("success", false);
            result.put("message", "Error: " + e.getMessage());
            response.getWriter().print(gson.toJson(result));
        }
    }
}
