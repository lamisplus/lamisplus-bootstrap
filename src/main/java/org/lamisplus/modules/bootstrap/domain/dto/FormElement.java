package org.lamisplus.modules.bootstrap.domain.dto;

import lombok.Data;

@Data
public class FormElement {
    private String name;
    private String location;
    private Integer priority = 1;
    private String componentId;
}
