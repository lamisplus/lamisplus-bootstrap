package org.lamisplus.modules.bootstrap.domain.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ModuleConfig {
    private String name;
    private String basePackage;
    private String version;
    private boolean store = true;
    private String summary;
    private String description;
    private List<Dependency> dependencies = new ArrayList<>();
    private List<Permission> permissions = new ArrayList<>();
    private List<Role> roles = new ArrayList<>();
    private String menuUrl;
    private String menuName;
}
