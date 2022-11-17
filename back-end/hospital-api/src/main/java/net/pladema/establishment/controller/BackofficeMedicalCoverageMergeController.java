package net.pladema.establishment.controller;

import net.pladema.establishment.controller.constraints.validator.permissions.BackofficeMedicalCoverageValidator;
import net.pladema.patient.controller.dto.BackofficeCoverageDto;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/medicalcoveragesmerge")
public class BackofficeMedicalCoverageMergeController extends AbstractBackofficeController<BackofficeCoverageDto, Integer> {

    private final BackofficeMedicalCoverageMergeStore medicalCoverageMergeStore;

    public BackofficeMedicalCoverageMergeController(
            BackofficeMedicalCoverageMergeStore backofficeMedicalCoverageMergeStore,
            BackofficeMedicalCoverageValidator medicalCoverageValidator
    ) {
        super(backofficeMedicalCoverageMergeStore, medicalCoverageValidator);
        this.medicalCoverageMergeStore = backofficeMedicalCoverageMergeStore;
    }

    @PutMapping("/{id}/baseMedicalCoverage/{baseMedicalCoverageId}")
    public @ResponseBody
    BackofficeCoverageDto update(@PathVariable("id") Integer id,
                                 @PathVariable("baseMedicalCoverageId") Integer baseMedicalCoverageId) {
        logger.debug("UPDATE[id={}] {}", id);
        return medicalCoverageMergeStore.merge(id, baseMedicalCoverageId);
    }

}