package com.example.parking.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.example.parking.dto.TarifaDTO;
import com.example.parking.entity.TarifaEntity;
import com.example.parking.mapper.TarifaMapper;
import com.example.parking.repository.TarifaRepository;
import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@SuppressWarnings("CallToPrintStackTrace")
@WebServlet("/api/tarifas")
public class TarifaAPIServlet extends HttpServlet {

    private TarifaRepository tarifaRepository;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        try {
            tarifaRepository = new TarifaRepository();
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
        response.setHeader("Access-Control-Allow-Origin", "*");
        PrintWriter out = response.getWriter();
        
        try {
            System.out.println("[TarifaAPIServlet] GET - Obteniendo tarifas");

            List<TarifaEntity> tarifas = tarifaRepository.findAll();
            List<TarifaDTO> tarifasDTO = tarifas.stream()
                    .map(TarifaMapper::toDTO)
                    .collect(Collectors.toList());
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("data", tarifasDTO);
            result.put("count", tarifasDTO.size());

            String json = gson.toJson(result);
            System.out.println("[TarifaAPIServlet] Respuesta con " + tarifasDTO.size() + " tarifas");
            out.print(json);
            out.flush();
            
        } catch (Exception e) {
            System.err.println("[TarifaAPIServlet] ERROR en GET: " + e.getMessage());
            e.printStackTrace();
            
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al obtener las tarifas: " + e.getMessage());
            out.print(gson.toJson(error));
            out.flush();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
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

            // CORRECCIÓN: Convertir a BigDecimal
            BigDecimal precio = new BigDecimal(precioStr);
            if (precio.compareTo(BigDecimal.ZERO) <= 0) {
                result.put("success", false);
                result.put("message", "El precio debe ser mayor a 0");
                out.print(gson.toJson(result));
                out.flush();
                return;
            }

            TarifaEntity tarifa = new TarifaEntity();
            tarifa.setTipo(tipo.trim());
            tarifa.setPrecioPorHora(precio);
            tarifa.setActiva(true);

            TarifaEntity saved = tarifaRepository.save(tarifa);

            if (saved != null) {
                result.put("success", true);
                result.put("message", "Tarifa registrada correctamente");
                result.put("data", TarifaMapper.toDTO(saved));
            } else {
                result.put("success", false);
                result.put("message", "No se pudo registrar la tarifa");
            }

            out.print(gson.toJson(result));
            out.flush();
            
        } catch (NumberFormatException e) {
            System.err.println("[TarifaAPIServlet] ERROR en formato de precio: " + e.getMessage());
            result.put("success", false);
            result.put("message", "Precio inválido: " + e.getMessage());
            out.print(gson.toJson(result));
            out.flush();
            
        } catch (Exception e) {
            System.err.println("[TarifaAPIServlet] ERROR en POST: " + e.getMessage());
            e.printStackTrace();
            
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.put("success", false);
            result.put("message", "Error interno: " + e.getMessage());
            out.print(gson.toJson(result));
            out.flush();
        }
    }
}