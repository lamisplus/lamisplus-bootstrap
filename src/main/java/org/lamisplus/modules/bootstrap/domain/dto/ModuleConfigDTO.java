package org.lamisplus.modules.bootstrap.domain.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ModuleConfigDTO {
    private String name;
    private String basePackage;
    private String version;
    private boolean store = true;
    private String summary;
    private String description;
    private List<DependencyDTO> dependencies = new ArrayList<>();
    private List<PermissionDTO> permissionDTOS = new ArrayList<>();
    private List<RoleDTO> roleDTOS = new ArrayList<>();
    private String menuUrl;
    private String menuName;
}
