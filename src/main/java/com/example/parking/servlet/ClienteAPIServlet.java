package com.example.parking.servlet;

import com.example.parking.dto.ClienteDTO;
import com.example.parking.entity.ClienteEntity;
import com.example.parking.mapper.ClienteMapper;
import com.example.parking.repository.ClienteRepository;
import com.google.gson.Gson;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@SuppressWarnings("CallToPrintStackTrace")
@WebServlet("/api/clientes")
@Tag(name = "Clientes", description = "Gestión de clientes del parqueadero")
public class ClienteAPIServlet extends HttpServlet {

    private ClienteRepository clienteRepository;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        clienteRepository = new ClienteRepository();
        gson = new Gson();
        System.out.println("[ClienteAPIServlet] Servlet inicializado correctamente");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        PrintWriter out = response.getWriter();
        
        try {
            // NUEVO: Soporte para obtener por ID
            String idParam = request.getParameter("id");
            
            if (idParam != null) {
                Integer id = Integer.parseInt(idParam);
                Optional<ClienteEntity> cliente = clienteRepository.findById(id);
                
                Map<String, Object> result = new HashMap<>();
                if (cliente.isPresent()) {
                    result.put("success", true);
                    result.put("data", ClienteMapper.toDTO(cliente.get()));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    result.put("success", false);
                    result.put("message", "Cliente no encontrado");
                }
                out.print(gson.toJson(result));
                return;
            }
            
            // Listar todos
            List<ClienteEntity> clientes = clienteRepository.findAll();
            
            List<ClienteDTO> clientesDTO = clientes.stream()
                .map(ClienteMapper::toDTO)
                .collect(Collectors.toList());
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("data", clientesDTO);
            result.put("count", clientesDTO.size());
            
            response.setStatus(HttpServletResponse.SC_OK);
            out.print(gson.toJson(result));
            
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "ID inválido");
            out.print(gson.toJson(error));
        } catch (Exception e) {
            System.err.println("[ClienteAPIServlet] ERROR: " + e.getMessage());
            e.printStackTrace();
            
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al obtener clientes: " + e.getMessage());
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
        
        String nombre = request.getParameter("nombre");
        String documento = request.getParameter("documento");
        String telefono = request.getParameter("telefono");
        String email = request.getParameter("email");
        String tipoClienteStr = request.getParameter("tipoCliente");
        String descuentoStr = request.getParameter("descuento");
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            if (nombre == null || nombre.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                result.put("success", false);
                result.put("message", "El nombre es requerido");
                out.print(gson.toJson(result));
                return;
            }
            
            if (documento == null || documento.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                result.put("success", false);
                result.put("message", "El documento es requerido");
                out.print(gson.toJson(result));
                return;
            }
            
            if (clienteRepository.findByDocumento(documento).isPresent()) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                result.put("success", false);
                result.put("message", "Ya existe un cliente con ese documento");
                out.print(gson.toJson(result));
                return;
            }
            
            ClienteEntity cliente = new ClienteEntity();
            cliente.setNombre(nombre.trim());
            cliente.setDocumento(documento.trim());
            cliente.setTelefono(telefono != null ? telefono.trim() : null);
            cliente.setEmail(email != null ? email.trim() : null);
            
            if (tipoClienteStr != null && !tipoClienteStr.trim().isEmpty()) {
                try {
                    cliente.setTipoCliente(ClienteEntity.TipoCliente.valueOf(tipoClienteStr.trim()));
                } catch (IllegalArgumentException e) {
                    cliente.setTipoCliente(ClienteEntity.TipoCliente.Eventual);
                }
            }
            
            if (descuentoStr != null && !descuentoStr.trim().isEmpty()) {
                try {
                    double descuentoDouble = Double.parseDouble(descuentoStr.trim());
                    if (descuentoDouble >= 0 && descuentoDouble <= 100) {
                        cliente.setDescuento(BigDecimal.valueOf(descuentoDouble));
                    } else {
                        cliente.setDescuento(BigDecimal.ZERO);
                    }
                } catch (NumberFormatException e) {
                    cliente.setDescuento(BigDecimal.ZERO);
                }
            } else {
                cliente.setDescuento(BigDecimal.ZERO);
            }
            
            ClienteEntity savedCliente = clienteRepository.save(cliente);
            ClienteDTO clienteDTO = ClienteMapper.toDTO(savedCliente);
            
            response.setStatus(HttpServletResponse.SC_CREATED);
            result.put("success", true);
            result.put("message", "Cliente registrado exitosamente");
            result.put("data", clienteDTO);
            out.print(gson.toJson(result));
            
        } catch (Exception e) {
            System.err.println("[ClienteAPIServlet] ERROR en POST: " + e.getMessage());
            e.printStackTrace();
            
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.put("success", false);
            result.put("message", "Error al registrar cliente: " + e.getMessage());
            out.print(gson.toJson(result));
        } finally {
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
        String nombre = request.getParameter("nombre");
        String telefono = request.getParameter("telefono");
        String email = request.getParameter("email");
        String tipoClienteStr = request.getParameter("tipoCliente");
        String descuentoStr = request.getParameter("descuento");
        
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
            Optional<ClienteEntity> clienteOpt = clienteRepository.findById(id);
            
            if (clienteOpt.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                result.put("success", false);
                result.put("message", "Cliente no encontrado");
                out.print(gson.toJson(result));
                return;
            }
            
            ClienteEntity cliente = clienteOpt.get();
            
            // Actualizar solo los campos proporcionados
            if (nombre != null && !nombre.trim().isEmpty()) {
                cliente.setNombre(nombre.trim());
            }
            
            if (telefono != null) {
                cliente.setTelefono(telefono.trim().isEmpty() ? null : telefono.trim());
            }
            
            if (email != null) {
                cliente.setEmail(email.trim().isEmpty() ? null : email.trim());
            }
            
            if (tipoClienteStr != null && !tipoClienteStr.trim().isEmpty()) {
                try {
                    cliente.setTipoCliente(ClienteEntity.TipoCliente.valueOf(tipoClienteStr.trim()));
                } catch (IllegalArgumentException e) {
                    // Mantener el valor actual si es inválido
                }
            }
            
            if (descuentoStr != null && !descuentoStr.trim().isEmpty()) {
                try {
                    double descuentoDouble = Double.parseDouble(descuentoStr.trim());
                    if (descuentoDouble >= 0 && descuentoDouble <= 100) {
                        cliente.setDescuento(BigDecimal.valueOf(descuentoDouble));
                    }
                } catch (NumberFormatException e) {
                    // Mantener el valor actual si es inválido
                }
            }
            
            ClienteEntity updated = clienteRepository.save(cliente);
            
            result.put("success", true);
            result.put("message", "Cliente actualizado correctamente");
            result.put("data", ClienteMapper.toDTO(updated));
            
            out.print(gson.toJson(result));
            
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            result.put("success", false);
            result.put("message", "ID inválido");
            out.print(gson.toJson(result));
        } catch (Exception e) {
            System.err.println("[ClienteAPIServlet] ERROR en PUT: " + e.getMessage());
            e.printStackTrace();
            
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.put("success", false);
            result.put("message", "Error al actualizar cliente: " + e.getMessage());
            out.print(gson.toJson(result));
        } finally {
            out.flush();
        }
    }

    @Override
    @SuppressWarnings("CallToPrintStackTrace")
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        String idStr = request.getParameter("id");
        Map<String, Object> result = new HashMap<>();
        
        try {
            if (idStr == null || idStr.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                result.put("success", false);
                result.put("message", "El ID es requerido");
                out.print(gson.toJson(result));
                return;
            }
            
            Integer id = Integer.parseInt(idStr);
            
            if (clienteRepository.findById(id).isEmpty()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                result.put("success", false);
                result.put("message", "Cliente no encontrado");
                out.print(gson.toJson(result));
                return;
            }
            
            clienteRepository.delete(id);
            
            response.setStatus(HttpServletResponse.SC_OK);
            result.put("success", true);
            result.put("message", "Cliente eliminado correctamente");
            out.print(gson.toJson(result));
            
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            result.put("success", false);
            result.put("message", "ID inválido");
            out.print(gson.toJson(result));
        } catch (Exception e) {
            System.err.println("[ClienteAPIServlet] ERROR en DELETE: " + e.getMessage());
            e.printStackTrace();
            
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.put("success", false);
            result.put("message", "Error al eliminar cliente: " + e.getMessage());
            out.print(gson.toJson(result));
        } finally {
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