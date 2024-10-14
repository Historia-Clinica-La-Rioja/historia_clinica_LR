package net.pladema.clinichistory.hospitalization.service.documents.validation;

import ar.lamansys.sgh.clinichistory.application.document.validators.GeneralDocumentValidator;
import ar.lamansys.sgh.clinichistory.domain.document.IDocumentBo;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionBo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.Collections;

@RequiredArgsConstructor
@Component
public class InternmentDocumentValidator {

    protected final GeneralDocumentValidator generalDocumentValidator;

    public void assertContextValid(IDocumentBo documentBo) {
        if (documentBo.getInstitutionId() == null)
            throw new ConstraintViolationException("El id de la institución es obligatorio", Collections.emptySet());
        if (documentBo.getEncounterId() == null)
            throw new ConstraintViolationException("El id del encuentro asociado es obligatorio", Collections.emptySet());
    }

    public void assertAnthropometricData(IDocumentBo documentBo) {
        assert generalDocumentValidator != null;
        generalDocumentValidator.assertAnthropometricData(documentBo);
    }

    public void assertEffectiveRiskFactorTimeValid(IDocumentBo documentBo, LocalDateTime entryDate) {
        assert generalDocumentValidator != null;
        generalDocumentValidator.assertEffectiveRiskFactorTimeValid(documentBo, entryDate);
    }

    public void assertDiagnosisValid(IDocumentBo documentBo, HealthConditionBo mainDiagnosis) {
        assert generalDocumentValidator != null;
        generalDocumentValidator.assertDiagnosisValid(documentBo, mainDiagnosis);
    }

    public void assertDocumentValid(IDocumentBo documentBo) {
        assert generalDocumentValidator != null;
        generalDocumentValidator.assertDocumentValid(documentBo);
    }

}
