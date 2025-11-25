CREATE DATABASE IF NOT EXISTS parkingDB CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE parkingDB;

-- Tabla Tarifa
CREATE TABLE IF NOT EXISTS Tarifa (
  id INT AUTO_INCREMENT PRIMARY KEY,
  tipo VARCHAR(50) NOT NULL UNIQUE,
  precio_por_hora DECIMAL(10,2) NOT NULL CHECK (precio_por_hora >= 0),
  activa BOOLEAN DEFAULT TRUE,
  creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  actualizado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_tipo (tipo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla Cliente
CREATE TABLE IF NOT EXISTS Cliente (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  documento VARCHAR(20) NOT NULL UNIQUE,
  telefono VARCHAR(20),
  email VARCHAR(100),
  tipo_cliente ENUM('Regular', 'VIP', 'Eventual') DEFAULT 'Eventual',
  descuento DECIMAL(5,2) DEFAULT 0.00,
  creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  actualizado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_documento (documento),
  INDEX idx_tipo_cliente (tipo_cliente)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla Vehiculo
CREATE TABLE IF NOT EXISTS Vehiculo (
  id INT AUTO_INCREMENT PRIMARY KEY,
  placa VARCHAR(15) NOT NULL UNIQUE,
  modelo VARCHAR(100),
  tipo VARCHAR(50) NOT NULL,
  cliente_id INT,
  ingreso DATETIME NOT NULL,
  salida DATETIME NULL,
  total_pagado DECIMAL(10,2) DEFAULT 0.00 CHECK (total_pagado >= 0),
  activo BOOLEAN DEFAULT TRUE,
  creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  actualizado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_placa (placa),
  INDEX idx_activo (activo),
  INDEX idx_tipo (tipo),
  INDEX idx_cliente (cliente_id),
  FOREIGN KEY (tipo) REFERENCES Tarifa(tipo) ON UPDATE CASCADE,
  FOREIGN KEY (cliente_id) REFERENCES Cliente(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla Historial
CREATE TABLE IF NOT EXISTS Historial (
  id INT AUTO_INCREMENT PRIMARY KEY,
  vehiculo_id INT NOT NULL,
  cliente_id INT,
  fecha_ingreso DATETIME NOT NULL,
  fecha_salida DATETIME NOT NULL,
  horas_totales DECIMAL(10,2) NOT NULL,
  tarifa_aplicada DECIMAL(10,2) NOT NULL,
  descuento_aplicado DECIMAL(5,2) DEFAULT 0.00,
  total_cobrado DECIMAL(10,2) NOT NULL,
  tipo_vehiculo VARCHAR(50) NOT NULL,
  placa VARCHAR(15) NOT NULL,
  creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_vehiculo (vehiculo_id),
  INDEX idx_cliente (cliente_id),
  INDEX idx_fecha_salida (fecha_salida),
  FOREIGN KEY (vehiculo_id) REFERENCES Vehiculo(id) ON DELETE CASCADE,
  FOREIGN KEY (cliente_id) REFERENCES Cliente(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Datos iniciales
INSERT INTO Tarifa (tipo, precio_por_hora, activa) VALUES 
('Carro', 5000.00, TRUE),
('Moto', 3000.00, TRUE),
('Bicicleta', 1000.00, TRUE),
('Camioneta', 6000.00, TRUE);

INSERT INTO Cliente (nombre, documento, telefono, email, tipo_cliente, descuento) VALUES
('Juan Pérez', '123456789', '3001234567', 'juan@example.com', 'Regular', 10.00),
('María González', '987654321', '3009876543', 'VIP', 20.00),
('Carlos Rodríguez', '456789123', '3004567891', 'Eventual', 0.00);

INSERT INTO Vehiculo (placa, modelo, tipo, cliente_id, ingreso, activo) VALUES
('ABC123', 'Toyota Corolla 2020', 'Carro', 1, NOW() - INTERVAL 2 HOUR, TRUE),
('XYZ789', 'Honda CBR 600', 'Moto', 2, NOW() - INTERVAL 1 HOUR, TRUE),
('DEF456', 'Trek Mountain Bike', 'Bicicleta', 3, NOW() - INTERVAL 30 MINUTE, TRUE);

-- Vista para reportes
CREATE OR REPLACE VIEW vista_vehiculos_activos AS
SELECT 
    v.id,
    v.placa,
    v.modelo,
    v.tipo,
    v.ingreso,
    c.nombre as cliente_nombre,
    c.tipo_cliente,
    c.descuento,
    t.precio_por_hora,
    TIMESTAMPDIFF(MINUTE, v.ingreso, NOW()) / 60.0 as horas_transcurridas,
    ROUND((TIMESTAMPDIFF(MINUTE, v.ingreso, NOW()) / 60.0) * t.precio_por_hora * (1 - c.descuento/100), 2) as costo_estimado
FROM Vehiculo v
LEFT JOIN Cliente c ON v.cliente_id = c.id
LEFT JOIN Tarifa t ON v.tipo = t.tipo
WHERE v.activo = TRUE;
