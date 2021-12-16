package net.pladema.establishment.controller;

import net.pladema.establishment.controller.constraints.validator.permissions.BackofficeClinicalSpecialtyCareLineValidator;
import net.pladema.establishment.repository.entity.ClinicalSpecialtyCareLine;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/clinicalspecialtycarelines")
public class BackofficeClinicalSpecialtyCareLineController extends AbstractBackofficeController<ClinicalSpecialtyCareLine, Integer> {

    public BackofficeClinicalSpecialtyCareLineController(BackofficeClinicalSpecialtyCareLineStore backofficeClinicalSpecialtyCareLineStore, BackofficeClinicalSpecialtyCareLineValidator backofficeClinicalSpecialtyCareLineValidator) {
        super(backofficeClinicalSpecialtyCareLineStore, backofficeClinicalSpecialtyCareLineValidator);
    }

}
