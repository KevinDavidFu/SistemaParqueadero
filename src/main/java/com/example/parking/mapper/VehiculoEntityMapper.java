package com.example.parking.mapper;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

import com.example.parking.dto.VehiculoDTO;
import com.example.parking.entity.VehiculoEntity;

public class VehiculoEntityMapper {
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public static VehiculoDTO toDTO(VehiculoEntity entity) {
        if (entity == null) {
            return null;
        }
        
        VehiculoDTO dto = new VehiculoDTO();
        dto.setId(entity.getId());
        dto.setPlaca(entity.getPlaca());
        dto.setModelo(entity.getModelo());
        dto.setTipo(entity.getTipo());
        
        if (entity.getIngreso() != null) {
            dto.setIngreso(entity.getIngreso().format(FORMATTER));
        }
        
        if (entity.getSalida() != null) {
            dto.setSalida(entity.getSalida().format(FORMATTER));
        }
        
        // CORRECCIÓN: Manejar BigDecimal correctamente
        BigDecimal totalPagado = entity.getTotalPagado();
        dto.setTotalPagado(totalPagado != null ? totalPagado : BigDecimal.ZERO);
        
        Boolean activo = entity.getActivo();
        dto.setActivo(activo != null ? activo : false);
        
        return dto;
    }

    public static VehiculoEntity toEntity(VehiculoDTO dto) {
        if (dto == null) {
            return null;
        }
        
        VehiculoEntity entity = new VehiculoEntity();
        entity.setId(dto.getId());
        entity.setPlaca(dto.getPlaca());
        entity.setModelo(dto.getModelo());
        entity.setTipo(dto.getTipo());
        entity.setTotalPagado(dto.getTotalPagado());
        entity.setActivo(dto.isActivo());
        
        return entity;
    }
}