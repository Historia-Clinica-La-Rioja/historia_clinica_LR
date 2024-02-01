package net.pladema.clinichistory.hospitalization.service.documents.validation;

import ar.lamansys.sgh.clinichistory.domain.ips.AnestheticTechniqueBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ClinicalTermsValidatorUtils;
import ar.lamansys.sgh.clinichistory.domain.ips.enums.EAnestheticTechnique;
import ar.lamansys.sgh.clinichistory.domain.ips.enums.EBreathing;
import ar.lamansys.sgh.clinichistory.domain.ips.enums.ECircuit;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class AnestheticTechniqueValidator {

    public void assertContextValid(List<AnestheticTechniqueBo> techniques) {

        this.assertNotDuplicated(techniques);

        techniques.stream()
                .peek(this::assertTechniqueIdField)
                .peek(this::assertTrachealIntubationFields)
                .peek(this::assertBreathingIdField)
                .forEach(this::assertCircuitIdField);
    }

    private void assertTechniqueIdField(AnestheticTechniqueBo technique) {
        Optional.ofNullable(technique.getTechniqueId())
                .ifPresent(EAnestheticTechnique::map);
    }

    private void assertTrachealIntubationFields(AnestheticTechniqueBo technique) {
        Boolean methodDefined = technique.getTrachealIntubation();
        List<Short> methods = technique.getTrachealIntubationMethodIds();
        if (((methodDefined && (methods == null || methods.isEmpty()))
        || (!methodDefined && methods != null && !methods.isEmpty())))
            throw new ConstraintViolationException("Campo intubación traqueal no válido", Collections.emptySet());
    }

    private void assertBreathingIdField(AnestheticTechniqueBo technique) {
        Optional.ofNullable(technique.getBreathingId())
                .ifPresent(EBreathing::map);
    }

    private void assertCircuitIdField(AnestheticTechniqueBo technique) {
        Optional.ofNullable(technique.getCircuitId())
                .ifPresent(ECircuit::map);
    }

    private void assertNotDuplicated(List<AnestheticTechniqueBo> techniques) {
        if (ClinicalTermsValidatorUtils.repeatedClinicalTerms(techniques))
            throw new ConstraintViolationException("Técnicas de anestesia repetidas", Collections.emptySet());
    }

}
