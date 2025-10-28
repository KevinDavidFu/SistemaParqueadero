package com.example.parking.servlet;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api-docs")
@OpenAPIDefinition(
    info = @Info(
        title = "Sistema de Parqueadero API",
        version = "1.0.0",
        description = "API RESTful para la gestión completa de un sistema de parqueadero, " +
                     "incluyendo vehículos, tarifas, clientes e historial de transacciones.",
        contact = @Contact(
            name = "Kevin David",
            email = "kevin@example.com"
        )
    ),
    servers = {
        @Server(url = "http://localhost:8080/SistemaParqueadero", description = "Servidor Local"),
        @Server(url = "http://localhost:9090/SistemaParqueadero", description = "Servidor Jetty")
    }
)
public class SwaggerServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(
            "<!DOCTYPE html>" +
            "<html><head><title>API Documentation</title></head>" +
            "<body style='font-family: Arial; max-width: 800px; margin: 50px auto; padding: 20px;'>" +
            "<h1>🚗 Sistema de Parqueadero - API REST</h1>" +
            "<h2>Documentación disponible:</h2>" +
            "<ul style='font-size: 18px; line-height: 2;'>" +
            "<li><a href='/SistemaParqueadero/swagger-ui/index.html'>📘 Swagger UI (Interfaz Interactiva)</a></li>" +
            "<li><a href='/SistemaParqueadero/api/vehiculos'>🚗 GET /api/vehiculos</a></li>" +
            "<li><a href='/SistemaParqueadero/api/tarifas'>💲 GET /api/tarifas</a></li>" +
            "<li><a href='/SistemaParqueadero/api/clientes'>👤 GET /api/clientes</a></li>" +
            "<li><a href='/SistemaParqueadero/health'>✅ Health Check</a></li>" +
            "</ul>" +
            "<h3>Endpoints Principales:</h3>" +
            "<table border='1' cellpadding='10' style='border-collapse: collapse; width: 100%;'>" +
            "<tr style='background: #007bff; color: white;'>" +
            "<th>Método</th><th>Endpoint</th><th>Descripción</th></tr>" +
            "<tr><td>GET</td><td>/api/vehiculos</td><td>Listar todos los vehículos</td></tr>" +
            "<tr><td>POST</td><td>/api/vehiculos</td><td>Registrar nuevo vehículo</td></tr>" +
            "<tr><td>DELETE</td><td>/api/vehiculos?placa={placa}</td><td>Eliminar vehículo</td></tr>" +
            "<tr><td>GET</td><td>/api/tarifas</td><td>Listar todas las tarifas</td></tr>" +
            "<tr><td>POST</td><td>/api/tarifas</td><td>Crear nueva tarifa</td></tr>" +
            "<tr><td>GET</td><td>/api/clientes</td><td>Listar todos los clientes</td></tr>" +
            "<tr><td>POST</td><td>/api/clientes</td><td>Registrar nuevo cliente</td></tr>" +
            "<tr><td>POST</td><td>/cobro?placa={placa}</td><td>Registrar salida y cobro</td></tr>" +
            "</table>" +
            "<p style='margin-top: 30px; padding: 15px; background: #fff3cd; border-radius: 5px;'>" +
            "<strong>Nota:</strong> Esta API utiliza Hibernate/JPA como ORM, " +
            "implementa el patrón Repository, y separa correctamente las capas con DTOs y Mappers." +
            "</p>" +
            "</body></html>"
        );
    }
}