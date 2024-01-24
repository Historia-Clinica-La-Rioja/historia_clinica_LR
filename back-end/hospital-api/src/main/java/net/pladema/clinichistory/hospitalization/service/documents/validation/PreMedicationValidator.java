package net.pladema.clinichistory.hospitalization.service.documents.validation;

import ar.lamansys.sgh.clinichistory.domain.ips.ClinicalTermsValidatorUtils;
import ar.lamansys.sgh.clinichistory.domain.ips.PreMedicationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.enums.EVia;
import java.util.Collections;
import java.util.List;
import javax.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class PreMedicationValidator {

    public void assertContextValid(List<PreMedicationBo> preMedications) {

        this.assertNotDuplicated(preMedications);

        preMedications.stream()
                .map(PreMedicationBo::getViaId)
                .forEach(this::assertViaValid);
    }

    public void assertViaValid(Short viaId) {
        EVia via = EVia.getById(viaId);
        if (!EVia.getPreMedication().contains(via))
            throw new ConstraintViolationException(String.format("Vía '%s' no válida como premedicación", via), Collections.emptySet());
        log.trace("Output -> isValid viaId {}", viaId);
    }

    public void assertNotDuplicated(List<PreMedicationBo> preMedications) {
        if (ClinicalTermsValidatorUtils.repeatedClinicalTerms(preMedications))
            throw new ConstraintViolationException("Pre-medicaciones repetidas", Collections.emptySet());
    }
}
