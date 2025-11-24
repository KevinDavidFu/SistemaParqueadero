package com.example.parking.mapper;

import com.example.parking.dto.ClienteDTO;
import com.example.parking.entity.ClienteEntity;
import java.math.BigDecimal;

/**
 * Mapper para convertir entre ClienteEntity y ClienteDTO
 */
public class ClienteMapper {
    
    /**
     * Convierte ClienteEntity a ClienteDTO
     */
    public static ClienteDTO toDTO(ClienteEntity entity) {
        if (entity == null) return null;
        
        // Convertir BigDecimal a Double para el DTO
        Double descuentoDouble = entity.getDescuento() != null 
            ? entity.getDescuento().doubleValue() 
            : 0.0;
        
        return new ClienteDTO(
            entity.getId(),
            entity.getNombre(),
            entity.getDocumento(),
            entity.getTelefono(),
            entity.getEmail(),
            entity.getTipoCliente() != null ? entity.getTipoCliente().name() : "Eventual",
            descuentoDouble
        );
    }
    
    /**
     * Convierte ClienteDTO a ClienteEntity
     */
    public static ClienteEntity toEntity(ClienteDTO dto) {
        if (dto == null) return null;
        
        ClienteEntity entity = new ClienteEntity();
        entity.setId(dto.getId());
        entity.setNombre(dto.getNombre());
        entity.setDocumento(dto.getDocumento());
        entity.setTelefono(dto.getTelefono());
        entity.setEmail(dto.getEmail());
        
        // Convertir String a Enum
        if (dto.getTipoCliente() != null && !dto.getTipoCliente().isEmpty()) {
            try {
                entity.setTipoCliente(ClienteEntity.TipoCliente.valueOf(dto.getTipoCliente()));
            } catch (IllegalArgumentException e) {
                entity.setTipoCliente(ClienteEntity.TipoCliente.Eventual);
            }
        }
        
        // Convertir Double a BigDecimal
        if (dto.getDescuento() != null) {
            entity.setDescuento(BigDecimal.valueOf(dto.getDescuento()));
        } else {
            entity.setDescuento(BigDecimal.ZERO);
        }
        
        return entity;
    }
}