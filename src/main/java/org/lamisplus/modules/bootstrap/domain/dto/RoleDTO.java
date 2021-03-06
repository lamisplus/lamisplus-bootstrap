package org.lamisplus.modules.bootstrap.domain.dto;

import lombok.Data;

import java.util.Set;

@Data
public class RoleDTO {
    Set<PermissionDTO> permissionDTOS;
    private String name;
    private String authority;
    private String description;
}
