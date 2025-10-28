package com.example.parking.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.parking.dto.VehiculoDTO;
import com.example.parking.mapper.VehiculoMapper;
import com.example.parking.model.Vehiculo;
import com.example.parking.service.ServicioVehiculo;
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

    private ServicioVehiculo servicioVehiculo;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        try {
            servicioVehiculo = new ServicioVehiculo();
            gson = new Gson();
            System.out.println("[VehiculoAPIServlet] Servlet inicializado correctamente");
        } catch (Exception e) {
            System.err.println("[VehiculoAPIServlet] ERROR al inicializar: " + e.getMessage());
            e.printStackTrace();
            throw new ServletException("Error al inicializar VehiculoAPIServlet", e);
        }
    }

    @Override
    @Operation(
        summary = "Listar todos los vehículos",
        description = "Obtiene la lista completa de vehículos registrados en el sistema",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de vehículos obtenida exitosamente",
                content = @Content(schema = @Schema(implementation = VehiculoDTO.class))
            ),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        }
    )
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        PrintWriter out = response.getWriter();
        
        try {
            System.out.println("[VehiculoAPIServlet] GET - Obteniendo vehículos");
            List<Vehiculo> vehiculos = servicioVehiculo.listarVehiculos();
            
            List<VehiculoDTO> vehiculosDTO = vehiculos.stream()
                .map(VehiculoMapper::toDTO)
                .collect(Collectors.toList());
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("data", vehiculosDTO);
            result.put("count", vehiculosDTO.size());
            
            String json = gson.toJson(result);
            System.out.println("[VehiculoAPIServlet] Respuesta con " + vehiculosDTO.size() + " vehículos");
            out.print(json);
            out.flush();
            
        } catch (SQLException e) {
            System.err.println("[VehiculoAPIServlet] ERROR SQL: " + e.getMessage());
            e.printStackTrace();
            
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error de base de datos: " + e.getMessage());
            out.print(gson.toJson(error));
            out.flush();
        }
    }

    @Override
    @Operation(
        summary = "Registrar nuevo vehículo",
        description = "Registra la entrada de un nuevo vehículo al parqueadero",
        parameters = {
            @Parameter(name = "placa", description = "Placa del vehículo", required = true),
            @Parameter(name = "modelo", description = "Modelo del vehículo"),
            @Parameter(name = "tipo", description = "Tipo de vehículo (Carro, Moto, Bicicleta)", required = true)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Vehículo registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        }
    )
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        String placa = request.getParameter("placa");
        String modelo = request.getParameter("modelo");
        String tipo = request.getParameter("tipo");
        
        System.out.println("[VehiculoAPIServlet] POST - placa: " + placa + ", tipo: " + tipo);

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
            out.flush();
            
        } catch (SQLException e) {
            System.err.println("[VehiculoAPIServlet] ERROR SQL en POST: " + e.getMessage());
            e.printStackTrace();
            
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.put("success", false);
            result.put("message", "Error de base de datos: " + e.getMessage());
            out.print(gson.toJson(result));
            out.flush();
        }
    }

    @Override
    @Operation(
        summary = "Eliminar vehículo",
        description = "Elimina un vehículo del sistema por su placa",
        parameters = {
            @Parameter(name = "placa", description = "Placa del vehículo a eliminar", required = true)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Vehículo eliminado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Placa no proporcionada"),
            @ApiResponse(responseCode = "404", description = "Vehículo no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        }
    )
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        String placa = request.getParameter("placa");
        
        System.out.println("[VehiculoAPIServlet] DELETE - placa: " + placa);
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            if (placa == null || placa.trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "La placa es requerida");
                out.print(gson.toJson(result));
                out.flush();
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
            out.flush();
            
        } catch (SQLException e) {
            System.err.println("[VehiculoAPIServlet] ERROR SQL en DELETE: " + e.getMessage());
            e.printStackTrace();
            
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.put("success", false);
            result.put("message", "Error de base de datos: " + e.getMessage());
            out.print(gson.toJson(result));
            out.flush();
        }
    }
}