DROP DATABASE IF EXISTS parkingDB;
CREATE DATABASE parkingDB CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE parkingDB;

-- Tabla Tarifa con índice único en tipo
CREATE TABLE Tarifa (
  id INT AUTO_INCREMENT PRIMARY KEY,
  tipo VARCHAR(50) NOT NULL UNIQUE,
  precio_por_hora DECIMAL(10,2) NOT NULL CHECK (precio_por_hora >= 0),
  creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  actualizado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_tipo (tipo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla Vehiculo con índices optimizados
CREATE TABLE Vehiculo (
  id INT AUTO_INCREMENT PRIMARY KEY,
  placa VARCHAR(15) NOT NULL UNIQUE,
  modelo VARCHAR(100),
  tipo VARCHAR(50) NOT NULL,
  ingreso DATETIME NOT NULL,
  salida DATETIME NULL,
  total_pagado DECIMAL(10,2) DEFAULT 0.00 CHECK (total_pagado >= 0),
  activo BOOLEAN DEFAULT TRUE,
  creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  actualizado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_placa (placa),
  INDEX idx_activo (activo),
  INDEX idx_tipo (tipo),
  FOREIGN KEY (tipo) REFERENCES Tarifa(tipo) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insertar tarifas iniciales
INSERT INTO Tarifa (tipo, precio_por_hora) VALUES 
('Carro', 5000.00),
('Moto', 3000.00),
('Bicicleta', 1000.00);

-- Insertar vehículos de prueba
INSERT INTO Vehiculo (placa, modelo, tipo, ingreso, activo) VALUES
('ABC123', 'Toyota Corolla 2020', 'Carro', NOW() - INTERVAL 2 HOUR, TRUE),
('XYZ789', 'Honda CBR 600', 'Moto', NOW() - INTERVAL 1 HOUR, TRUE),
('DEF456', 'Trek Mountain Bike', 'Bicicleta', NOW() - INTERVAL 30 MINUTE, TRUE);

-- Verificar datos
SELECT 'Tarifas:' as Tabla;
SELECT * FROM Tarifa;
SELECT 'Vehículos:' as Tabla;
SELECT * FROM Vehiculo;