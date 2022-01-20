package net.pladema.establishment.controller;

import net.pladema.establishment.controller.constraints.validator.permissions.BackofficeMedicalCoverageValidator;
import net.pladema.patient.controller.dto.BackofficeCoverageDto;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/medicalcoveragesmerge")
public class BackofficeMedicalCoverageMergeController extends AbstractBackofficeController<BackofficeCoverageDto, Integer> {

    public BackofficeMedicalCoverageMergeController(
            BackofficeMedicalCoverageMergeStore backofficeMedicalCoverageMergeStore,
            BackofficeMedicalCoverageValidator medicalCoverageValidator
    ) {
        super(backofficeMedicalCoverageMergeStore, medicalCoverageValidator);
    }


}