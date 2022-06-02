package net.pladema.clinichistory.hospitalization.service.documents.validation;

import ar.lamansys.sgh.clinichistory.domain.document.IDocumentBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ClinicalTermsValidatorUtils;
import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosisBo;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionBo;

import javax.validation.ConstraintViolationException;

import java.time.LocalDateTime;
import java.util.Collections;

public class InternmentDocumentValidator {

	public void assertContextValid(IDocumentBo documentBo) {
		if (documentBo.getInstitutionId() == null)
			throw new ConstraintViolationException("El id de la institución es obligatorio", Collections.emptySet());
		if (documentBo.getEncounterId() == null)
			throw new ConstraintViolationException("El id del encuentro asociado es obligatorio", Collections.emptySet());
	}

	public void assertAnthropometricData(IDocumentBo documentBo) {
		var validator = new AnthropometricDataValidator();
		validator.isValid(documentBo);
	}

	public void assertEffectiveRiskFactorTimeValid(IDocumentBo documentBo, LocalDateTime entryDate) {
		var validator = new EffectiveRiskFactorTimeValidator();
		validator.isValid(documentBo, entryDate);
	}

	public void assertDiagnosisValid(IDocumentBo documentBo, HealthConditionBo mainDiagnosis) {
		if (documentBo.getMainDiagnosis() != null && (documentBo.getDiagnosis() == null || documentBo.getDiagnosis().isEmpty() || !documentBo.getMainDiagnosis().getSnomed().equals(mainDiagnosis.getSnomed())))
			return;
		if (documentBo.getDiagnosis().stream()
				.map(DiagnosisBo::getSnomed)
				.anyMatch(d -> d.equals(mainDiagnosis.getSnomed()))) {
			throw new ConstraintViolationException("Diagnostico principal duplicado en los secundarios", Collections.emptySet());
		}
	}

	public void assertDocumentValid(IDocumentBo documentBo) {
		if (ClinicalTermsValidatorUtils.repeatedClinicalTerms(documentBo.getDiagnosis()))
			throw new ConstraintViolationException("Diagnósticos secundarios repetidos", Collections.emptySet());
		if (ClinicalTermsValidatorUtils.repeatedClinicalTerms(documentBo.getPersonalHistories()))
			throw new ConstraintViolationException("Antecedentes personales repetidos", Collections.emptySet());
		if (ClinicalTermsValidatorUtils.repeatedClinicalTerms(documentBo.getFamilyHistories()))
			throw new ConstraintViolationException("Antecedentes familiares repetidos", Collections.emptySet());
		if (ClinicalTermsValidatorUtils.repeatedClinicalTerms(documentBo.getProcedures()))
			throw new ConstraintViolationException("Procedimientos repetidos", Collections.emptySet());
	}
}
