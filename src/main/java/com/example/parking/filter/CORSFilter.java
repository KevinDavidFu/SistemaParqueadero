package com.example.parking.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filtro CORS para permitir peticiones desde el frontend
 * Configurado para desarrollo local
 */
@WebFilter(urlPatterns = "/*")
public class CORSFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        
        
        res.setHeader("Access-Control-Allow-Origin", "*");
        
        // Métodos HTTP permitidos
        res.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        
        // Headers permitidos
        res.setHeader("Access-Control-Allow-Headers", 
            "Content-Type, Authorization, X-Requested-With, Accept");
        
        // Permite cookies/credenciales
        res.setHeader("Access-Control-Allow-Credentials", "true");
        
        // Tiempo de caché para preflight requests
        res.setHeader("Access-Control-Max-Age", "3600");
        
        // Si es una petición OPTIONS (preflight), responder inmediatamente
        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
            res.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        
        // Continuar con la petición normal
        chain.doFilter(request, response);
    }
}