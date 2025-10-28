package com.example.parking.mapper;

import java.time.format.DateTimeFormatter;

import com.example.parking.dto.VehiculoDTO;
import com.example.parking.model.Vehiculo;

public class VehiculoMapper {
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public static VehiculoDTO toDTO(Vehiculo vehiculo) {
        if (vehiculo == null) {
            return null;
        }
        
        VehiculoDTO dto = new VehiculoDTO();
        dto.setId(vehiculo.getId());
        dto.setPlaca(vehiculo.getPlaca());
        dto.setModelo(vehiculo.getModelo());
        dto.setTipo(vehiculo.getTipo());
        
        if (vehiculo.getIngreso() != null) {
            dto.setIngreso(vehiculo.getIngreso().format(FORMATTER));
        }
        
        if (vehiculo.getSalida() != null) {
            dto.setSalida(vehiculo.getSalida().format(FORMATTER));
        }
        
        dto.setTotalPagado(vehiculo.getTotalPagado());
        dto.setActivo(vehiculo.isActivo());
        
        return dto;
    }

    public static Vehiculo toEntity(VehiculoDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setId(dto.getId());
        vehiculo.setPlaca(dto.getPlaca());
        vehiculo.setModelo(dto.getModelo());
        vehiculo.setTipo(dto.getTipo());
        vehiculo.setTotalPagado(dto.getTotalPagado());
        vehiculo.setActivo(dto.isActivo());
        
        return vehiculo;
    }
}