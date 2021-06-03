package net.pladema.person.controller;

import net.pladema.person.repository.PersonExtendedRepository;
import net.pladema.person.repository.entity.PersonExtended;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/personextended")
public class BackofficePersonExtendedController extends AbstractBackofficeController<PersonExtended, Integer> {

    public BackofficePersonExtendedController(PersonExtendedRepository personExtendedRepository) {
        super(personExtendedRepository);
    }

}
