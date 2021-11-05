package net.pladema.nomivac.infrastructure.input.rest.controller;

import net.pladema.nomivac.infrastructure.output.immunization.repository.NomivacImmunizationSync;
import net.pladema.nomivac.infrastructure.output.immunization.repository.NomivacImmunizationSyncRepository;
import net.pladema.sgx.backoffice.repository.BackofficeRepository;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.sgx.backoffice.rest.BackofficeEntityValidatorAdapter;
import net.pladema.sgx.backoffice.rest.BackofficePermissionValidatorAdapter;

import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/nomivac-immunizationsync")
public class BackofficeNomivacImmunizationSyncController extends AbstractBackofficeController<NomivacImmunizationSync, Integer> {

    public BackofficeNomivacImmunizationSyncController(NomivacImmunizationSyncRepository repository) {
        super(
            new BackofficeRepository<>(repository),
            new BackofficePermissionValidatorAdapter<>(HttpMethod.GET, HttpMethod.PUT),
            new BackofficeEntityValidatorAdapter<>()
        );
    }

}
