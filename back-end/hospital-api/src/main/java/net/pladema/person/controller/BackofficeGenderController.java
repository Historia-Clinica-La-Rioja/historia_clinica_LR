package net.pladema.person.controller;

import net.pladema.person.repository.GenderRepository;
import net.pladema.person.repository.entity.Gender;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/genders")
public class BackofficeGenderController extends AbstractBackofficeController<Gender, Short> {

    public BackofficeGenderController(GenderRepository repository) {
        super(repository);
    }

}
