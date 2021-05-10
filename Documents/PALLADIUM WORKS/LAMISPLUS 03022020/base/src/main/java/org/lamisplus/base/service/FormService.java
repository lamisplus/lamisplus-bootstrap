package org.lamisplus.base.service;

import lombok.extern.slf4j.Slf4j;
import org.lamisplus.base.domain.entities.Country;
import org.lamisplus.base.domain.entities.Form;
import org.lamisplus.base.repositories.CountriesRepository;
import org.lamisplus.base.repositories.FormRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
public class FormService {
    private final FormRepository formRepository;

    public FormService(FormRepository formRepository) {

        this.formRepository = formRepository;
    }

    public List<Form> getAllForm() {
        List<Form> forms = this.formRepository.findAll();

        List<Form> formList = new ArrayList();
        forms.forEach(oneForm -> {
            Form form = new Form();

            if(oneForm.getServiceName().equals("GENERAL_SERVICE"))
                return;

            form.setName(oneForm.getName());
            form.setId(oneForm.getId());
            form.setResourcePath(oneForm.getResourcePath());
            form.setServiceName(oneForm.getServiceName());
            form.setVersion(oneForm.getVersion());
            form.setDisplayName(oneForm.getDisplayName());

            formList.add(form);

        });

        return formList;
    }

}
