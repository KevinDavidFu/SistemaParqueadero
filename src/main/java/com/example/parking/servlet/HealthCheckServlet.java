package com.example.parking.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import com.example.parking.util.DBUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/health")
public class HealthCheckServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Health Check</title></head><body>");
        out.println("<h1>Sistema de Parqueadero - Health Check</h1>");
        
        out.println("<h2>Estado del Sistema:</h2>");
        out.println("<ul>");
        out.println("<li><strong>Servlet:</strong> OK ✓</li>");
        
        boolean dbOk = DBUtil.testConnection();
        out.println("<li><strong>Base de Datos:</strong> " + (dbOk ? "OK ✓" : "ERROR ✗") + "</li>");
        
        out.println("</ul>");
        
        if (!dbOk) {
            out.println("<p style='color:red;'><strong>ERROR:</strong> No se puede conectar a la base de datos. Verifica:</p>");
            out.println("<ol>");
            out.println("<li>MySQL está corriendo</li>");
            out.println("<li>La base de datos 'parkingDB' existe</li>");
            out.println("<li>Las credenciales en application.properties son correctas</li>");
            out.println("</ol>");
        }
        
        out.println("</body></html>");
    }
}