package net.pladema.clinichistory.hospitalization.service.documents.validation;

import ar.lamansys.sgh.clinichistory.domain.ips.AnestheticSubstanceBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ClinicalTermsValidatorUtils;
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

    public void assertContextValid(List<AnestheticSubstanceBo> preMedications) {

        this.assertNotDuplicated(preMedications);

        preMedications.stream()
                .peek(this::assertViaValid)
                .filter(substance -> EVia.OTHER.getId().equals(substance.getViaId()))
                .forEach(this::assertOtherViaValid);
    }

    public void assertOtherViaValid(AnestheticSubstanceBo substance) {
        String note = substance.getViaNote();
        if (note == null || note.isBlank())
            throw new ConstraintViolationException(String.format("La descripción asociada a la vía %s no puede estar en blanco", EVia.OTHER.getDescription()), Collections.emptySet());
    }

    public void assertViaValid(AnestheticSubstanceBo substance) {
        EVia via = EVia.getById(substance.getViaId());
        if (!EVia.getPreMedication().contains(via))
            throw new ConstraintViolationException(String.format("Vía '%s' no válida como premedicación", via.getDescription()), Collections.emptySet());
    }

    public void assertNotDuplicated(List<AnestheticSubstanceBo> preMedications) {
        if (ClinicalTermsValidatorUtils.repeatedClinicalTerms(preMedications))
            throw new ConstraintViolationException("Pre-medicaciones repetidas", Collections.emptySet());
    }
}
