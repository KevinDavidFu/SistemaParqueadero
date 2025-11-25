package com.example.parking.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.parking.dto.VehiculoDTO;
import com.example.parking.entity.VehiculoEntity;
import com.example.parking.mapper.VehiculoEntityMapper;
import com.example.parking.repository.VehiculoRepository;
import com.google.gson.Gson;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api/vehiculos")
@Tag(name = "Vehículos", description = "Gestión de vehículos en el parqueadero")
public class VehiculoAPIServlet extends HttpServlet {

    private VehiculoRepository vehiculoRepository;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        try {
            vehiculoRepository = new VehiculoRepository();
            gson = new Gson();
            System.out.println("[VehiculoAPIServlet] Servlet inicializado correctamente");
        } catch (Exception e) {
            System.err.println("[VehiculoAPIServlet] ERROR al inicializar: " + e.getMessage());
            e.printStackTrace();
            throw new ServletException("Error al inicializar VehiculoAPIServlet", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        PrintWriter out = response.getWriter();
        
        try {
            // NUEVO: Soporte para obtener por ID o placa
            String idParam = request.getParameter("id");
            String placaParam = request.getParameter("placa");
            
            if (idParam != null) {
                // Obtener por ID
                Integer id = Integer.parseInt(idParam);
                Optional<VehiculoEntity> vehiculo = vehiculoRepository.findById(id);
                
                Map<String, Object> result = new HashMap<>();
                if (vehiculo.isPresent()) {
                    result.put("success", true);
                    result.put("data", VehiculoEntityMapper.toDTO(vehiculo.get()));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    result.put("success", false);
                    result.put("message", "Vehículo no encontrado");
                }
                out.print(gson.toJson(result));
                return;
            }
            
            if (placaParam != null) {
                // Obtener por placa
                Optional<VehiculoEntity> vehiculo = vehiculoRepository.findByPlaca(placaParam);
                
                Map<String, Object> result = new HashMap<>();
                if (vehiculo.isPresent()) {
                    result.put("success", true);
                    result.put("data", VehiculoEntityMapper.toDTO(vehiculo.get()));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    result.put("success", false);
                    result.put("message", "Vehículo no encontrado");
                }
                out.print(gson.toJson(result));
                return;
            }
            
            // Listar todos
            System.out.println("[VehiculoAPIServlet] GET - Obteniendo todos los vehículos");
            List<VehiculoEntity> vehiculos = vehiculoRepository.findAll();
            List<VehiculoDTO> vehiculosDTO = vehiculos.stream()
                    .map(VehiculoEntityMapper::toDTO)
                    .collect(Collectors.toList());
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("data", vehiculosDTO);
            result.put("count", vehiculosDTO.size());
            
            out.print(gson.toJson(result));
            
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "ID inválido");
            out.print(gson.toJson(error));
        } catch (Exception e) {
            System.err.println("[VehiculoAPIServlet] ERROR en GET: " + e.getMessage());
            e.printStackTrace();
            
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al obtener vehículos: " + e.getMessage());
            out.print(gson.toJson(error));
        } finally {
            out.flush();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        String placa = request.getParameter("placa");
        String modelo = request.getParameter("modelo");
        String tipo = request.getParameter("tipo");
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            if (placa == null || placa.trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "La placa es requerida");
                out.print(gson.toJson(result));
                out.flush();
                return;
            }
            
            if (tipo == null || tipo.trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "El tipo de vehículo es requerido");
                out.print(gson.toJson(result));
                out.flush();
                return;
            }

            VehiculoEntity vehiculo = new VehiculoEntity();
            vehiculo.setPlaca(placa.toUpperCase().trim());
            vehiculo.setModelo(modelo != null ? modelo.trim() : "");
            vehiculo.setTipo(tipo.trim());
            vehiculo.setIngreso(LocalDateTime.now());
            vehiculo.setActivo(true);

            VehiculoEntity saved = vehiculoRepository.save(vehiculo);

            result.put("success", true);
            result.put("message", "Vehículo registrado correctamente");
            result.put("data", VehiculoEntityMapper.toDTO(saved));
            
            out.print(gson.toJson(result));
            out.flush();
            
        } catch (Exception e) {
            System.err.println("[VehiculoAPIServlet] ERROR en POST: " + e.getMessage());
            e.printStackTrace();
            
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.put("success", false);
            result.put("message", "Error al registrar vehículo: " + e.getMessage());
            out.print(gson.toJson(result));
            out.flush();
        }
    }

    // NUEVO: Método doPut para actualizar
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        PrintWriter out = response.getWriter();
        
        String idParam = request.getParameter("id");
        String modelo = request.getParameter("modelo");
        String tipo = request.getParameter("tipo");
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            if (idParam == null || idParam.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                result.put("success", false);
                result.put("message", "El ID es requerido");
                out.print(gson.toJson(result));
                return;
            }
            
            Integer id = Integer.parseInt(idParam);
            Optional<VehiculoEntity> vehiculoOpt = vehiculoRepository.findById(id);
            
            if (vehiculoOpt.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                result.put("success", false);
                result.put("message", "Vehículo no encontrado");
                out.print(gson.toJson(result));
                return;
            }
            
            VehiculoEntity vehiculo = vehiculoOpt.get();
            
            // Actualizar solo los campos proporcionados
            if (modelo != null && !modelo.trim().isEmpty()) {
                vehiculo.setModelo(modelo.trim());
            }
            
            if (tipo != null && !tipo.trim().isEmpty()) {
                vehiculo.setTipo(tipo.trim());
            }
            
            VehiculoEntity updated = vehiculoRepository.save(vehiculo);
            
            result.put("success", true);
            result.put("message", "Vehículo actualizado correctamente");
            result.put("data", VehiculoEntityMapper.toDTO(updated));
            
            out.print(gson.toJson(result));
            
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            result.put("success", false);
            result.put("message", "ID inválido");
            out.print(gson.toJson(result));
        } catch (Exception e) {
            System.err.println("[VehiculoAPIServlet] ERROR en PUT: " + e.getMessage());
            e.printStackTrace();
            
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.put("success", false);
            result.put("message", "Error al actualizar vehículo: " + e.getMessage());
            out.print(gson.toJson(result));
        } finally {
            out.flush();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        String placa = request.getParameter("placa");
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            if (placa == null || placa.trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "La placa es requerida");
                out.print(gson.toJson(result));
                out.flush();
                return;
            }

            Optional<VehiculoEntity> vehiculoOpt = vehiculoRepository.findByPlaca(placa);
            if (vehiculoOpt.isPresent()) {
                vehiculoRepository.delete(vehiculoOpt.get().getId());
                result.put("success", true);
                result.put("message", "Vehículo eliminado correctamente");
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                result.put("success", false);
                result.put("message", "Vehículo no encontrado");
            }
            out.print(gson.toJson(result));
            out.flush();
            
        } catch (Exception e) {
            System.err.println("[VehiculoAPIServlet] ERROR en DELETE: " + e.getMessage());
            e.printStackTrace();
            
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.put("success", false);
            result.put("message", "Error al eliminar vehículo: " + e.getMessage());
            out.print(gson.toJson(result));
            out.flush();
        }
    }
    
    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}