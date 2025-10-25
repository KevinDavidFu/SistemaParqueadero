package com.example.parking.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.parking.model.Vehiculo;
import com.example.parking.service.ServicioVehiculo;
import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api/vehiculos")
public class VehiculoAPIServlet extends HttpServlet {

    private ServicioVehiculo servicioVehiculo;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        servicioVehiculo = new ServicioVehiculo();
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        
        try (PrintWriter out = response.getWriter()) {
            List<Vehiculo> vehiculos = servicioVehiculo.listarVehiculos();
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("data", vehiculos);
            result.put("count", vehiculos.size());
            out.print(gson.toJson(result));
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al obtener vehículos: " + e.getMessage());
            response.getWriter().print(gson.toJson(error));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json;charset=UTF-8");
        
        String placa = request.getParameter("placa");
        String modelo = request.getParameter("modelo");
        String tipo = request.getParameter("tipo");

        Map<String, Object> result = new HashMap<>();
        
        try (PrintWriter out = response.getWriter()) {
            // Validaciones
            if (placa == null || placa.trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "La placa es requerida");
                out.print(gson.toJson(result));
                return;
            }
            
            if (tipo == null || tipo.trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "El tipo de vehículo es requerido");
                out.print(gson.toJson(result));
                return;
            }

            boolean insertado = servicioVehiculo.registrarVehiculo(
                placa.toUpperCase().trim(), 
                modelo != null ? modelo.trim() : "", 
                tipo.trim()
            );

            if (insertado) {
                result.put("success", true);
                result.put("message", "Vehículo registrado correctamente");
            } else {
                result.put("success", false);
                result.put("message", "No se pudo registrar el vehículo");
            }
            out.print(gson.toJson(result));
            
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.put("success", false);
            result.put("message", "Error: " + e.getMessage());
            response.getWriter().print(gson.toJson(result));
        }
    }
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
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

            boolean eliminado = servicioVehiculo.eliminarVehiculo(placa);
        
            if (eliminado) {
                result.put("success", true);
                result.put("message", "Vehículo eliminado correctamente");
            } else {
                result.put("success", false);
                result.put("message", "No se pudo eliminar el vehículo");
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