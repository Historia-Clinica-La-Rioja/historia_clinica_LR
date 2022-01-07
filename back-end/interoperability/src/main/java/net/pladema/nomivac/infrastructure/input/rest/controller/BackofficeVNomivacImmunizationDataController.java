package net.pladema.nomivac.infrastructure.input.rest.controller;

import net.pladema.nomivac.infrastructure.output.immunization.repository.VNomivacImmunizationData;
import net.pladema.nomivac.infrastructure.output.immunization.repository.VNomivacImmunizationDataRepository;
import net.pladema.sgx.backoffice.repository.BackofficeRepository;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.sgx.backoffice.rest.BackofficeEntityValidatorAdapter;
import net.pladema.sgx.backoffice.rest.BackofficePermissionValidatorAdapter;

import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/nomivac-immunizationdata")
public class BackofficeVNomivacImmunizationDataController extends AbstractBackofficeController<VNomivacImmunizationData, Integer> {

    public BackofficeVNomivacImmunizationDataController(VNomivacImmunizationDataRepository repository) {
        super(new BackofficeRepository<>(repository),
                new BackofficePermissionValidatorAdapter<>(HttpMethod.GET, HttpMethod.PUT),
                new BackofficeEntityValidatorAdapter<>());
    }
}
