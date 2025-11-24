package com.example.parking.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.parking.dto.VehiculoDTO;
import com.example.parking.entity.TarifaEntity;
import com.example.parking.entity.VehiculoEntity;
import com.example.parking.mapper.VehiculoEntityMapper;
import com.example.parking.repository.TarifaRepository;
import com.example.parking.repository.VehiculoRepository;

/**
 * Servicio de negocio para gestión de vehículos
 * Contiene toda la lógica de negocio relacionada con vehículos
 */
public class VehiculoService {
    
    private final VehiculoRepository vehiculoRepository;
    private final TarifaRepository tarifaRepository;
    
    public VehiculoService() {
        this.vehiculoRepository = new VehiculoRepository();
        this.tarifaRepository = new TarifaRepository();
    }
    
    /**
     * Lista todos los vehículos
     */
    public List<VehiculoDTO> listarTodos() {
        List<VehiculoEntity> vehiculos = vehiculoRepository.findAll();
        return vehiculos.stream()
                .map(VehiculoEntityMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Lista vehículos activos (en el parqueadero)
     */
    public List<VehiculoDTO> listarActivos() {
        List<VehiculoEntity> vehiculos = vehiculoRepository.findByActivo(true);
        return vehiculos.stream()
                .map(VehiculoEntityMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Busca un vehículo por placa
     */
    public Optional<VehiculoDTO> buscarPorPlaca(String placa) {
        return vehiculoRepository.findByPlaca(placa)
                .map(VehiculoEntityMapper::toDTO);
    }
    
    /**
     * Registra la entrada de un vehículo
     */
    public VehiculoDTO registrarEntrada(String placa, String modelo, String tipo, Integer clienteId) {
        // Validar que el tipo de vehículo tenga tarifa
        Optional<TarifaEntity> tarifaOpt = tarifaRepository.findByTipo(tipo);
        if (tarifaOpt.isEmpty()) {
            throw new IllegalArgumentException("No existe tarifa para el tipo de vehículo: " + tipo);
        }
        
        // Validar que la placa no esté ya registrada como activa
        Optional<VehiculoEntity> existente = vehiculoRepository.findByPlaca(placa);
        if (existente.isPresent() && existente.get().getActivo()) {
            throw new IllegalStateException("El vehículo ya está registrado en el parqueadero");
        }
        
        // Crear nuevo vehículo
        VehiculoEntity vehiculo = new VehiculoEntity();
        vehiculo.setPlaca(placa.toUpperCase().trim());
        vehiculo.setModelo(modelo != null ? modelo.trim() : "");
        vehiculo.setTipo(tipo.trim());
        vehiculo.setIngreso(LocalDateTime.now());
        vehiculo.setActivo(true);
        vehiculo.setTotalPagado(0.0);
        
        VehiculoEntity saved = vehiculoRepository.save(vehiculo);
        return VehiculoEntityMapper.toDTO(saved);
    }
    
    /**
     * Registra la salida de un vehículo y calcula el cobro
     */
    public CobroResultDTO registrarSalida(String placa) {
        // Buscar vehículo
        Optional<VehiculoEntity> vehiculoOpt = vehiculoRepository.findByPlaca(placa);
        if (vehiculoOpt.isEmpty()) {
            throw new IllegalArgumentException("Vehículo no encontrado");
        }
        
        VehiculoEntity vehiculo = vehiculoOpt.get();
        
        if (!vehiculo.getActivo()) {
            throw new IllegalStateException("El vehículo ya tiene registrada su salida");
        }
        
        // Obtener tarifa
        Optional<TarifaEntity> tarifaOpt = tarifaRepository.findByTipo(vehiculo.getTipo());
        if (tarifaOpt.isEmpty()) {
            throw new IllegalStateException("No se encontró tarifa para: " + vehiculo.getTipo());
        }
        
        TarifaEntity tarifa = tarifaOpt.get();
        
        // Calcular tiempo y costo
        LocalDateTime ahora = LocalDateTime.now();
        Duration duracion = Duration.between(vehiculo.getIngreso(), ahora);
        double horas = duracion.toMinutes() / 60.0;
        
        // Mínimo 1 hora
        if (horas < 1.0) {
            horas = 1.0;
        }
        
        double total = Math.round(horas * tarifa.getPrecioPorHora() * 100.0) / 100.0;
        
        // Actualizar vehículo
        vehiculo.setSalida(ahora);
        vehiculo.setTotalPagado(total);
        vehiculo.setActivo(false);
        vehiculoRepository.save(vehiculo);
        
        // Retornar resultado
        CobroResultDTO resultado = new CobroResultDTO();
        resultado.setPlaca(placa);
        resultado.setHoras(Math.round(horas * 100.0) / 100.0);
        resultado.setPrecioPorHora(tarifa.getPrecioPorHora());
        resultado.setTotal(total);
        resultado.setIngreso(vehiculo.getIngreso());
        resultado.setSalida(ahora);
        
        return resultado;
    }
    
    /**
     * Elimina un vehículo por placa
     */
    public boolean eliminar(String placa) {
        Optional<VehiculoEntity> vehiculo = vehiculoRepository.findByPlaca(placa);
        if (vehiculo.isPresent()) {
            vehiculoRepository.delete(vehiculo.get().getId());
            return true;
        }
        return false;
    }
    
    /**
     * DTO para resultado de cobro
     */
    public static class CobroResultDTO {
        private String placa;
        private double horas;
        private double precioPorHora;
        private double total;
        private LocalDateTime ingreso;
        private LocalDateTime salida;
        
        // Getters y Setters
        public String getPlaca() { return placa; }
        public void setPlaca(String placa) { this.placa = placa; }
        
        public double getHoras() { return horas; }
        public void setHoras(double horas) { this.horas = horas; }
        
        public double getPrecioPorHora() { return precioPorHora; }
        public void setPrecioPorHora(double precioPorHora) { this.precioPorHora = precioPorHora; }
        
        public double getTotal() { return total; }
        public void setTotal(double total) { this.total = total; }
        
        public LocalDateTime getIngreso() { return ingreso; }
        public void setIngreso(LocalDateTime ingreso) { this.ingreso = ingreso; }
        
        public LocalDateTime getSalida() { return salida; }
        public void setSalida(LocalDateTime salida) { this.salida = salida; }
    }
}