package org.lamisplus.base.rest;

import lombok.extern.slf4j.Slf4j;
import org.lamisplus.base.domain.entities.Form;

import org.lamisplus.base.service.FormService;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("/api/forms")
@Slf4j
public class FormController {
    private final FormService formService;

    public FormController(FormService formService) {

        this.formService = formService;
    }

    @GetMapping("/all")
    public List<Form> findAllForms() {
        return this.formService.getAllForm();
    }


}
