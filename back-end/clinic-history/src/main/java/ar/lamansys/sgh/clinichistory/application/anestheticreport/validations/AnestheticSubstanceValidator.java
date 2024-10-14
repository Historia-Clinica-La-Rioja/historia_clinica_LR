package ar.lamansys.sgh.clinichistory.application.anestheticreport.validations;

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
public class AnestheticSubstanceValidator {

    public void assertSnomedAndDosageAndViaFields(List<AnestheticSubstanceBo> substances) {

        this.assertSnomed(substances);

        substances.stream()
                .peek(this::assertDosageField)
                .forEach(this::assertViaValid);
    }

    public void assertSnomedAndDosageFields(List<? extends AnestheticSubstanceBo> substances) {

        this.assertSnomed(substances);

        substances.forEach(this::assertDosageField);
    }

    public void assertSnomed(List<? extends AnestheticSubstanceBo> substances) {
        this.assertNotDuplicated(substances);
    }

    private void assertDosageField(AnestheticSubstanceBo substance) {
        if (substance.getDosage() == null
                || substance.getDosage().getQuantity() == null
                || substance.getDosage().getQuantity().hasAnyNullValues())
            throw new ConstraintViolationException("Indicar la dosis es obligatorio", Collections.emptySet());
    }

    private void assertOtherViaValid(AnestheticSubstanceBo substance) {
        String note = substance.getViaNote();
        if (note == null || note.isBlank())
            throw new ConstraintViolationException(String.format("La descripción asociada a la vía %s no puede estar en blanco", EVia.OTHER.getDescription()), Collections.emptySet());
    }

    private void assertViaValid(AnestheticSubstanceBo substance) {
        Short viaId = substance.getViaId();
        EVia via = EVia.getById(viaId);
        if (!substance.getStaticDefinedVias().contains(via))
            throw new ConstraintViolationException(String.format("Vía ingresada '%s' no es válida en '%s'", via.getDescription(), substance.getDescriptionType()), Collections.emptySet());

        if (EVia.OTHER.getId().equals(viaId))
            this.assertOtherViaValid(substance);
    }

    private void assertNotDuplicated(List<? extends AnestheticSubstanceBo> substances) {
        if (ClinicalTermsValidatorUtils.repeatedClinicalTerms(substances))
            throw new ConstraintViolationException(String.format("Sustancias de '%s' repetidas", substances.get(0).getDescriptionType()), Collections.emptySet());
    }
}
