package com.example.parking.mapper;

import com.example.parking.dto.ClienteDTO;
import com.example.parking.entity.ClienteEntity;
import java.math.BigDecimal; // Importamos para referencia, aunque no es estrictamente necesario si ya está en la Entity/DTO

public class ClienteMapper {
    
    public static ClienteDTO toDTO(ClienteEntity entity) {
        if (entity == null) return null;
        
        return new ClienteDTO(
            entity.getId(),
            entity.getNombre(),
            entity.getDocumento(),
            entity.getTelefono(),
            entity.getEmail(),
            entity.getTipoCliente() != null ? entity.getTipoCliente().name() : null,
            // Línea 18: Ambos getDescuento() deben devolver BigDecimal
            entity.getDescuento() 
        );
    }
    
    public static ClienteEntity toEntity(ClienteDTO dto) {
        if (dto == null) return null;
        
        ClienteEntity entity = new ClienteEntity();
        entity.setId(dto.getId());
        entity.setNombre(dto.getNombre());
        entity.setDocumento(dto.getDocumento());
        entity.setTelefono(dto.getTelefono());
        entity.setEmail(dto.getEmail());
        
        if (dto.getTipoCliente() != null) {
            entity.setTipoCliente(ClienteEntity.TipoCliente.valueOf(dto.getTipoCliente()));
        }
        
        // Línea 36: Ambos setDescuento() y getDescuento() deben usar BigDecimal
        entity.setDescuento(dto.getDescuento());
        return entity;
    }
}