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
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api/tarifas")
public class TarifaAPIServlet extends HttpServlet {

    private ServicioTarifa servicioTarifa;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        try {
            servicioTarifa = new ServicioTarifa();
            gson = new Gson();
            System.out.println("[TarifaAPIServlet] Servlet inicializado correctamente");
        } catch (Exception e) {
            System.err.println("[TarifaAPIServlet] ERROR al inicializar: " + e.getMessage());
            e.printStackTrace();
            throw new ServletException("Error al inicializar TarifaAPIServlet", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            System.out.println("[TarifaAPIServlet] GET - Obteniendo tarifas");
            List<Tarifa> tarifas = servicioTarifa.listarTarifas();
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("data", tarifas);
            
            String json = gson.toJson(result);
            System.out.println("[TarifaAPIServlet] Respuesta: " + json);
            out.print(json);
            out.flush();
            
        } catch (SQLException e) {
            System.err.println("[TarifaAPIServlet] ERROR SQL: " + e.getMessage());
            e.printStackTrace();
            
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error de base de datos: " + e.getMessage());
            out.print(gson.toJson(error));
            out.flush();
            
        } catch (Exception e) {
            System.err.println("[TarifaAPIServlet] ERROR INESPERADO: " + e.getMessage());
            e.printStackTrace();
            
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error interno: " + e.getMessage());
            out.print(gson.toJson(error));
            out.flush();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        String tipo = request.getParameter("tipo");
        String precioStr = request.getParameter("precioPorHora");
        
        System.out.println("[TarifaAPIServlet] POST - tipo: " + tipo + ", precio: " + precioStr);

        Map<String, Object> result = new HashMap<>();
        
        try {
            if (tipo == null || tipo.trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "El tipo de vehículo es requerido");
                out.print(gson.toJson(result));
                out.flush();
                return;
            }
            
            if (precioStr == null || precioStr.trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "El precio es requerido");
                out.print(gson.toJson(result));
                out.flush();
                return;
            }

            double precio = Double.parseDouble(precioStr);
            if (precio <= 0) {
                result.put("success", false);
                result.put("message", "El precio debe ser mayor a 0");
                out.print(gson.toJson(result));
                out.flush();
                return;
            }

            servicioTarifa.agregarTarifa(tipo.trim(), precio);
            result.put("success", true);
            result.put("message", "Tarifa registrada correctamente");
            out.print(gson.toJson(result));
            out.flush();
            
        } catch (NumberFormatException e) {
            result.put("success", false);
            result.put("message", "Precio inválido: " + e.getMessage());
            out.print(gson.toJson(result));
            out.flush();
            
        } catch (SQLException e) {
            System.err.println("[TarifaAPIServlet] ERROR SQL en POST: " + e.getMessage());
            e.printStackTrace();
            
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.put("success", false);
            result.put("message", "Error de base de datos: " + e.getMessage());
            out.print(gson.toJson(result));
            out.flush();
        }
    }
}