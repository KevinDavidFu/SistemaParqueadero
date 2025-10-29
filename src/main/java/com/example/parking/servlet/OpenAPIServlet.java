package com.example.parking.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/openapi.json")
public class OpenAPIServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        String openApiJson = """
        {
          "openapi": "3.0.3",
          "info": {
            "title": "Sistema de Parqueadero API",
            "version": "1.0.0",
            "description": "API REST completa para gestión de parqueadero con vehículos, tarifas y clientes",
            "contact": {
              "name": "Kevin David",
              "email": "kevin@example.com"
            }
          },
          "servers": [
            {
              "url": "http://localhost:9090/SistemaParqueadero",
              "description": "Servidor Local Jetty"
            }
          ],
          "paths": {
            "/api/vehiculos": {
              "get": {
                "tags": ["Vehículos"],
                "summary": "Listar todos los vehículos",
                "description": "Obtiene la lista completa de vehículos registrados",
                "responses": {
                  "200": {
                    "description": "Lista de vehículos",
                    "content": {
                      "application/json": {
                        "schema": {
                          "type": "object",
                          "properties": {
                            "success": { "type": "boolean" },
                            "data": {
                              "type": "array",
                              "items": { "$ref": "#/components/schemas/Vehiculo" }
                            },
                            "count": { "type": "integer" }
                          }
                        }
                      }
                    }
                  }
                }
              },
              "post": {
                "tags": ["Vehículos"],
                "summary": "Registrar nuevo vehículo",
                "requestBody": {
                  "content": {
                    "application/x-www-form-urlencoded": {
                      "schema": {
                        "type": "object",
                        "properties": {
                          "placa": { "type": "string", "example": "ABC123" },
                          "modelo": { "type": "string", "example": "Toyota Corolla 2020" },
                          "tipo": { "type": "string", "example": "Carro" }
                        },
                        "required": ["placa", "tipo"]
                      }
                    }
                  }
                },
                "responses": {
                  "200": { "description": "Vehículo registrado exitosamente" }
                }
              }
            },
            "/api/tarifas": {
              "get": {
                "tags": ["Tarifas"],
                "summary": "Listar todas las tarifas",
                "responses": {
                  "200": {
                    "description": "Lista de tarifas",
                    "content": {
                      "application/json": {
                        "schema": {
                          "type": "object",
                          "properties": {
                            "success": { "type": "boolean" },
                            "data": {
                              "type": "array",
                              "items": { "$ref": "#/components/schemas/Tarifa" }
                            }
                          }
                        }
                      }
                    }
                  }
                }
              },
              "post": {
                "tags": ["Tarifas"],
                "summary": "Crear nueva tarifa",
                "requestBody": {
                  "content": {
                    "application/x-www-form-urlencoded": {
                      "schema": {
                        "type": "object",
                        "properties": {
                          "tipo": { "type": "string", "example": "Carro" },
                          "precioPorHora": { "type": "number", "example": 5000 }
                        },
                        "required": ["tipo", "precioPorHora"]
                      }
                    }
                  }
                },
                "responses": {
                  "200": { "description": "Tarifa creada exitosamente" }
                }
              }
            },
            "/api/clientes": {
              "get": {
                "tags": ["Clientes"],
                "summary": "Listar todos los clientes",
                "responses": {
                  "200": {
                    "description": "Lista de clientes",
                    "content": {
                      "application/json": {
                        "schema": {
                          "type": "object",
                          "properties": {
                            "success": { "type": "boolean" },
                            "data": {
                              "type": "array",
                              "items": { "$ref": "#/components/schemas/Cliente" }
                            }
                          }
                        }
                      }
                    }
                  }
                }
              }
            },
            "/cobro": {
              "post": {
                "tags": ["Operaciones"],
                "summary": "Registrar salida y cobro",
                "requestBody": {
                  "content": {
                    "application/x-www-form-urlencoded": {
                      "schema": {
                        "type": "object",
                        "properties": {
                          "placa": { "type": "string", "example": "ABC123" }
                        },
                        "required": ["placa"]
                      }
                    }
                  }
                },
                "responses": {
                  "200": { "description": "Cobro registrado exitosamente" }
                }
              }
            }
          },
          "components": {
            "schemas": {
              "Vehiculo": {
                "type": "object",
                "properties": {
                  "id": { "type": "integer" },
                  "placa": { "type": "string" },
                  "modelo": { "type": "string" },
                  "tipo": { "type": "string" },
                  "ingreso": { "type": "string", "format": "date-time" },
                  "salida": { "type": "string", "format": "date-time" },
                  "totalPagado": { "type": "number" },
                  "activo": { "type": "boolean" }
                }
              },
              "Tarifa": {
                "type": "object",
                "properties": {
                  "id": { "type": "integer" },
                  "tipo": { "type": "string" },
                  "precioPorHora": { "type": "number" },
                  "activa": { "type": "boolean" },
                  "creadoEn": { "type": "string", "format": "date-time" }
                }
              },
              "Cliente": {
                "type": "object",
                "properties": {
                  "id": { "type": "integer" },
                  "nombre": { "type": "string" },
                  "documento": { "type": "string" },
                  "telefono": { "type": "string" },
                  "email": { "type": "string" },
                  "tipoCliente": { "type": "string", "enum": ["Regular", "VIP", "Eventual"] },
                  "descuento": { "type": "number" }
                }
              }
            }
          }
        }
        """;
        
        out.print(openApiJson);
    }
}