package com.example.parking.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.parking.model.Tarifa;
import com.example.parking.service.ServicioTarifa;
import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class TarifaAPIServlet extends HttpServlet {

    private ServicioTarifa servicioTarifa;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        servicioTarifa = new ServicioTarifa();
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json;charset=UTF-8");
        
        try (PrintWriter out = response.getWriter()) {
            List<Tarifa> tarifas = servicioTarifa.listarTarifas();
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("data", tarifas);
            out.print(gson.toJson(result));
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error: " + e.getMessage());
            response.getWriter().print(gson.toJson(error));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json;charset=UTF-8");
        
        String tipo = request.getParameter("tipo");
        String precioStr = request.getParameter("precioPorHora");

        Map<String, Object> result = new HashMap<>();
        
        try (PrintWriter out = response.getWriter()) {
            if (tipo == null || tipo.trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "El tipo de vehículo es requerido");
                out.print(gson.toJson(result));
                return;
            }
            
            if (precioStr == null || precioStr.trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "El precio es requerido");
                out.print(gson.toJson(result));
                return;
            }

            double precio = Double.parseDouble(precioStr);
            if (precio <= 0) {
                result.put("success", false);
                result.put("message", "El precio debe ser mayor a 0");
                out.print(gson.toJson(result));
                return;
            }

            servicioTarifa.agregarTarifa(tipo.trim(), precio);
            result.put("success", true);
            result.put("message", "Tarifa registrada correctamente");
            out.print(gson.toJson(result));
            
        } catch (NumberFormatException e) {
            result.put("success", false);
            result.put("message", "Precio inválido");
            response.getWriter().print(gson.toJson(result));
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.put("success", false);
            result.put("message", "Error: " + e.getMessage());
            response.getWriter().print(gson.toJson(result));
        }
    }
}