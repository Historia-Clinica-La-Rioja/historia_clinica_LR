package net.pladema.emergencycare.service.impl;

import ar.lamansys.sgh.clinichistory.application.createDocument.DocumentFactory;
import ar.lamansys.sgh.clinichistory.domain.ips.ClinicalTermsValidatorUtils;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import lombok.AllArgsConstructor;
import net.pladema.clinichistory.outpatient.createoutpatient.service.exceptions.CreateOutpatientDocumentException;
import net.pladema.clinichistory.outpatient.createoutpatient.service.exceptions.CreateOutpatientDocumentExceptionEnum;
import net.pladema.emergencycare.service.CreateEmergencyCareEvolutionNoteDocumentService;
import net.pladema.emergencycare.service.domain.EmergencyCareEvolutionNoteDocumentBo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;

import java.time.LocalDateTime;
import java.util.Collections;

@Service
@AllArgsConstructor
public class CreateEmergencyCareEvolutionNoteDocumentServiceImpl implements CreateEmergencyCareEvolutionNoteDocumentService {

	private static final Logger LOG = LoggerFactory.getLogger(CreateEmergencyCareEvolutionNoteDocumentServiceImpl.class);

	public static final String OUTPUT = "Output -> {}";

	private final DocumentFactory documentFactory;

	private final DateTimeProvider dateTimeProvider;

	private final UpdateEmergencyCareEvolutionNoteService updateEmergencyCareEvolutionNoteService;

	@Override
	public EmergencyCareEvolutionNoteDocumentBo execute(EmergencyCareEvolutionNoteDocumentBo emergencyCareEvolutionNoteDocument, Integer emergencyCareEvolutionNoteId) {
		LOG.debug("Input parameters -> emergencyCareEvolutionNoteDocument {}", emergencyCareEvolutionNoteDocument);
		assertContextValid(emergencyCareEvolutionNoteDocument);
		LocalDateTime now = dateTimeProvider.nowDateTime();
		emergencyCareEvolutionNoteDocument.setPerformedDate(now);
		emergencyCareEvolutionNoteDocument.setId(documentFactory.run(emergencyCareEvolutionNoteDocument, true));
		updateEmergencyCareEvolutionNoteService.updateDocumentId(emergencyCareEvolutionNoteId, emergencyCareEvolutionNoteDocument.getId());
		LOG.debug(OUTPUT, emergencyCareEvolutionNoteDocument);
		return emergencyCareEvolutionNoteDocument;
	}

	private void assertContextValid(EmergencyCareEvolutionNoteDocumentBo emergencyCareEvolutionNoteDocument) {
		if (emergencyCareEvolutionNoteDocument.getInstitutionId() == null)
			throw new ConstraintViolationException("El id de la institución es obligatorio", Collections.emptySet());
		if (emergencyCareEvolutionNoteDocument.getEncounterId() == null)
			throw new ConstraintViolationException("El id del encuentro asociado es obligatorio", Collections.emptySet());

		CreateOutpatientDocumentException repeatedErrors = new CreateOutpatientDocumentException(CreateOutpatientDocumentExceptionEnum.REPEATED_CLINICAL_TERM);
		if (ClinicalTermsValidatorUtils.repeatedClinicalTerms(emergencyCareEvolutionNoteDocument.getReasons()))
			repeatedErrors.addError("Motivos repetidos");

		if (ClinicalTermsValidatorUtils.repeatedClinicalTerms(emergencyCareEvolutionNoteDocument.getProblems()))
			repeatedErrors.addError("Problemas médicos repetidos");

		if (ClinicalTermsValidatorUtils.repeatedClinicalTerms(emergencyCareEvolutionNoteDocument.getFamilyHistories().getContent()))
			repeatedErrors.addError("Antecedentes familiares repetidos");

		if (ClinicalTermsValidatorUtils.repeatedClinicalTerms(emergencyCareEvolutionNoteDocument.getAllergies().getContent()))
			repeatedErrors.addError("Alergias repetidas");

		if (ClinicalTermsValidatorUtils.repeatedClinicalTerms(emergencyCareEvolutionNoteDocument.getProcedures()))
			repeatedErrors.addError("Procedimientos repetidos");
		if (repeatedErrors.hasErrors())
			throw repeatedErrors;
	}

}
