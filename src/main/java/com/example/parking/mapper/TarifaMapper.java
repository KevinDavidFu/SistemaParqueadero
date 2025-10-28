package com.example.parking.mapper;

import java.time.format.DateTimeFormatter;

import com.example.parking.dto.TarifaDTO;
import com.example.parking.entity.TarifaEntity;

public class TarifaMapper {
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    
    public static TarifaDTO toDTO(TarifaEntity entity) {
        if (entity == null) return null;
        
        return new TarifaDTO(
            entity.getId(),
            entity.getTipo(),
            entity.getPrecioPorHora(),
            entity.getActiva(),
            entity.getCreadoEn() != null ? entity.getCreadoEn().format(FORMATTER) : null
        );
    }
    
    public static TarifaEntity toEntity(TarifaDTO dto) {
        if (dto == null) return null;
        
        TarifaEntity entity = new TarifaEntity();
        entity.setId(dto.getId());
        entity.setTipo(dto.getTipo());
        entity.setPrecioPorHora(dto.getPrecioPorHora());
        entity.setActiva(dto.getActiva());
        return entity;
    }
}