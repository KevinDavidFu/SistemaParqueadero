package com.example.parking.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

public class VehiculoService {
    
    private final VehiculoRepository vehiculoRepository;
    private final TarifaRepository tarifaRepository;
    
    public VehiculoService() {
        this.vehiculoRepository = new VehiculoRepository();
        this.tarifaRepository = new TarifaRepository();
    }
    
    public List<VehiculoDTO> listarTodos() {
        List<VehiculoEntity> vehiculos = vehiculoRepository.findAll();
        return vehiculos.stream()
                .map(VehiculoEntityMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<VehiculoDTO> listarActivos() {
        List<VehiculoEntity> vehiculos = vehiculoRepository.findByActivo(true);
        return vehiculos.stream()
                .map(VehiculoEntityMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    public Optional<VehiculoDTO> buscarPorPlaca(String placa) {
        return vehiculoRepository.findByPlaca(placa)
                .map(VehiculoEntityMapper::toDTO);
    }
    
    public VehiculoDTO registrarEntrada(String placa, String modelo, String tipo, Integer clienteId) {
        Optional<TarifaEntity> tarifaOpt = tarifaRepository.findByTipo(tipo);
        if (tarifaOpt.isEmpty()) {
            throw new IllegalArgumentException("No existe tarifa para el tipo de vehículo: " + tipo);
        }
        
        Optional<VehiculoEntity> existente = vehiculoRepository.findByPlaca(placa);
        if (existente.isPresent() && existente.get().getActivo()) {
            throw new IllegalStateException("El vehículo ya está registrado en el parqueadero");
        }
        
        VehiculoEntity vehiculo = new VehiculoEntity();
        vehiculo.setPlaca(placa.toUpperCase().trim());
        vehiculo.setModelo(modelo != null ? modelo.trim() : "");
        vehiculo.setTipo(tipo.trim());
        vehiculo.setIngreso(LocalDateTime.now());
        vehiculo.setActivo(true);
        vehiculo.setTotalPagado(BigDecimal.ZERO);
        
        VehiculoEntity saved = vehiculoRepository.save(vehiculo);
        return VehiculoEntityMapper.toDTO(saved);
    }
    
    public CobroResultDTO registrarSalida(String placa) {
        Optional<VehiculoEntity> vehiculoOpt = vehiculoRepository.findByPlaca(placa);
        if (vehiculoOpt.isEmpty()) {
            throw new IllegalArgumentException("Vehículo no encontrado");
        }
        
        VehiculoEntity vehiculo = vehiculoOpt.get();
        
        if (!vehiculo.getActivo()) {
            throw new IllegalStateException("El vehículo ya tiene registrada su salida");
        }
        
        Optional<TarifaEntity> tarifaOpt = tarifaRepository.findByTipo(vehiculo.getTipo());
        if (tarifaOpt.isEmpty()) {
            throw new IllegalStateException("No se encontró tarifa para: " + vehiculo.getTipo());
        }
        
        TarifaEntity tarifa = tarifaOpt.get();
        
        // Calcular tiempo y costo con BigDecimal
        LocalDateTime ahora = LocalDateTime.now();
        Duration duracion = Duration.between(vehiculo.getIngreso(), ahora);
        
        BigDecimal horas = BigDecimal.valueOf(duracion.toMinutes())
                .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);
        
        // Mínimo 1 hora
        if (horas.compareTo(BigDecimal.ONE) < 0) {
            horas = BigDecimal.ONE;
        }
        
        BigDecimal total = horas.multiply(tarifa.getPrecioPorHora())
                .setScale(2, RoundingMode.HALF_UP);
        
        // Actualizar vehículo
        vehiculo.setSalida(ahora);
        vehiculo.setTotalPagado(total);
        vehiculo.setActivo(false);
        vehiculoRepository.save(vehiculo);
        
        // Retornar resultado
        CobroResultDTO resultado = new CobroResultDTO();
        resultado.setPlaca(placa);
        resultado.setHoras(horas.doubleValue());
        resultado.setPrecioPorHora(tarifa.getPrecioPorHora().doubleValue());
        resultado.setTotal(total.doubleValue());
        resultado.setIngreso(vehiculo.getIngreso());
        resultado.setSalida(ahora);
        
        return resultado;
    }
    
    public boolean eliminar(String placa) {
        Optional<VehiculoEntity> vehiculo = vehiculoRepository.findByPlaca(placa);
        if (vehiculo.isPresent()) {
            vehiculoRepository.delete(vehiculo.get().getId());
            return true;
        }
        return false;
    }
    
    public static class CobroResultDTO {
        private String placa;
        private double horas;
        private double precioPorHora;
        private double total;
        private LocalDateTime ingreso;
        private LocalDateTime salida;
        
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