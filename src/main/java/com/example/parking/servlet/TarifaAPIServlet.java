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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@SuppressWarnings("CallToPrintStackTrace")
@WebServlet("/api/tarifas")
@Tag(name = "Tarifas", description = "Gestión completa de tarifas del parqueadero")
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
    @Operation(
        summary = "Listar todas las tarifas",
        description = "Obtiene la lista completa de tarifas registradas"
    )
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        PrintWriter out = response.getWriter();
        
        try {
            // Verificar si se solicita una tarifa específica por ID
            String idParam = request.getParameter("id");
            
            if (idParam != null) {
                // GET por ID
                Integer id = Integer.parseInt(idParam);
                Optional<TarifaEntity> tarifaOpt = tarifaRepository.findById(id);
                
                if (tarifaOpt.isPresent()) {
                    Map<String, Object> result = new HashMap<>();
                    result.put("success", true);
                    result.put("data", TarifaMapper.toDTO(tarifaOpt.get()));
                    out.print(gson.toJson(result));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    Map<String, Object> error = new HashMap<>();
                    error.put("success", false);
                    error.put("message", "Tarifa no encontrada");
                    out.print(gson.toJson(error));
                }
            } else {
                // GET todas las tarifas
                List<TarifaEntity> tarifas = tarifaRepository.findAll();
                List<TarifaDTO> tarifasDTO = tarifas.stream()
                        .map(TarifaMapper::toDTO)
                        .collect(Collectors.toList());
                
                Map<String, Object> result = new HashMap<>();
                result.put("success", true);
                result.put("data", tarifasDTO);
                result.put("count", tarifasDTO.size());
                out.print(gson.toJson(result));
            }
            
            out.flush();
            
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "ID inválido");
            out.print(gson.toJson(error));
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
    @Operation(
        summary = "Crear nueva tarifa",
        description = "Registra una nueva tarifa en el sistema"
    )
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        PrintWriter out = response.getWriter();
        
        String tipo = request.getParameter("tipo");
        String precioStr = request.getParameter("precioPorHora");

        Map<String, Object> result = new HashMap<>();
        
        try {
            if (tipo == null || tipo.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                result.put("success", false);
                result.put("message", "El tipo de vehículo es requerido");
                out.print(gson.toJson(result));
                out.flush();
                return;
            }
            
            if (precioStr == null || precioStr.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                result.put("success", false);
                result.put("message", "El precio es requerido");
                out.print(gson.toJson(result));
                out.flush();
                return;
            }

            BigDecimal precio = new BigDecimal(precioStr);
            if (precio.compareTo(BigDecimal.ZERO) <= 0) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                result.put("success", false);
                result.put("message", "El precio debe ser mayor a 0");
                out.print(gson.toJson(result));
                out.flush();
                return;
            }

            // Verificar si ya existe una tarifa con ese tipo
            Optional<TarifaEntity> existente = tarifaRepository.findByTipo(tipo.trim());
            if (existente.isPresent()) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                result.put("success", false);
                result.put("message", "Ya existe una tarifa para ese tipo de vehículo");
                out.print(gson.toJson(result));
                out.flush();
                return;
            }

            TarifaEntity tarifa = new TarifaEntity();
            tarifa.setTipo(tipo.trim());
            tarifa.setPrecioPorHora(precio);
            tarifa.setActiva(true);

            TarifaEntity saved = tarifaRepository.save(tarifa);

            response.setStatus(HttpServletResponse.SC_CREATED);
            result.put("success", true);
            result.put("message", "Tarifa registrada correctamente");
            result.put("data", TarifaMapper.toDTO(saved));
            out.print(gson.toJson(result));
            out.flush();
            
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            result.put("success", false);
            result.put("message", "Precio inválido");
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

    @Override
    @Operation(
        summary = "Actualizar tarifa",
        description = "Actualiza una tarifa existente",
        parameters = {
            @Parameter(name = "id", description = "ID de la tarifa", required = true),
            @Parameter(name = "precioPorHora", description = "Nuevo precio por hora"),
            @Parameter(name = "activa", description = "Estado activo/inactivo")
        }
    )
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        PrintWriter out = response.getWriter();
        
        String idParam = request.getParameter("id");
        String precioStr = request.getParameter("precioPorHora");
        String activaStr = request.getParameter("activa");

        Map<String, Object> result = new HashMap<>();
        
        try {
            if (idParam == null || idParam.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                result.put("success", false);
                result.put("message", "El ID es requerido");
                out.print(gson.toJson(result));
                out.flush();
                return;
            }

            Integer id = Integer.parseInt(idParam);
            Optional<TarifaEntity> tarifaOpt = tarifaRepository.findById(id);
            
            if (tarifaOpt.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                result.put("success", false);
                result.put("message", "Tarifa no encontrada");
                out.print(gson.toJson(result));
                out.flush();
                return;
            }

            TarifaEntity tarifa = tarifaOpt.get();

            // Actualizar precio si se proporciona
            if (precioStr != null && !precioStr.trim().isEmpty()) {
                BigDecimal precio = new BigDecimal(precioStr);
                if (precio.compareTo(BigDecimal.ZERO) <= 0) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    result.put("success", false);
                    result.put("message", "El precio debe ser mayor a 0");
                    out.print(gson.toJson(result));
                    out.flush();
                    return;
                }
                tarifa.setPrecioPorHora(precio);
            }

            // Actualizar estado si se proporciona
            if (activaStr != null && !activaStr.trim().isEmpty()) {
                tarifa.setActiva(Boolean.parseBoolean(activaStr));
            }

            TarifaEntity updated = tarifaRepository.update(tarifa);

            result.put("success", true);
            result.put("message", "Tarifa actualizada correctamente");
            result.put("data", TarifaMapper.toDTO(updated));
            out.print(gson.toJson(result));
            out.flush();
            
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            result.put("success", false);
            result.put("message", "Parámetros inválidos");
            out.print(gson.toJson(result));
            out.flush();
        } catch (Exception e) {
            System.err.println("[TarifaAPIServlet] ERROR en PUT: " + e.getMessage());
            e.printStackTrace();
            
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.put("success", false);
            result.put("message", "Error al actualizar tarifa: " + e.getMessage());
            out.print(gson.toJson(result));
            out.flush();
        }
    }

    @Override
    @Operation(
        summary = "Eliminar tarifa",
        description = "Elimina una tarifa del sistema",
        parameters = {
            @Parameter(name = "id", description = "ID de la tarifa", required = true)
        }
    )
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        PrintWriter out = response.getWriter();
        
        String idParam = request.getParameter("id");
        Map<String, Object> result = new HashMap<>();
        
        try {
            if (idParam == null || idParam.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                result.put("success", false);
                result.put("message", "El ID es requerido");
                out.print(gson.toJson(result));
                out.flush();
                return;
            }

            Integer id = Integer.parseInt(idParam);
            
            if (tarifaRepository.findById(id).isEmpty()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                result.put("success", false);
                result.put("message", "Tarifa no encontrada");
                out.print(gson.toJson(result));
                out.flush();
                return;
            }

            tarifaRepository.delete(id);
            
            result.put("success", true);
            result.put("message", "Tarifa eliminada correctamente");
            out.print(gson.toJson(result));
            out.flush();
            
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            result.put("success", false);
            result.put("message", "ID inválido");
            out.print(gson.toJson(result));
            out.flush();
        } catch (Exception e) {
            System.err.println("[TarifaAPIServlet] ERROR en DELETE: " + e.getMessage());
            e.printStackTrace();
            
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.put("success", false);
            result.put("message", "Error al eliminar tarifa: " + e.getMessage());
            out.print(gson.toJson(result));
            out.flush();
        }
    }
}