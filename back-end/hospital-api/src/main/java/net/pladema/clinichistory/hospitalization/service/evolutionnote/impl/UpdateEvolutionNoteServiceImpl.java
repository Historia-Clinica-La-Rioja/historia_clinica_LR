package net.pladema.clinichistory.hospitalization.service.evolutionnote.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.lamansys.sgh.clinichistory.application.createDocument.DocumentFactory;
import ar.lamansys.sgh.clinichistory.domain.ips.ClinicalTerm;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.EDocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.AllergyIntoleranceClinicalStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.AllergyIntoleranceVerificationStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ConditionClinicalStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ConditionVerificationStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.InmunizationStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ProceduresStatus;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedDocumentPort;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.service.InternmentDocumentModificationValidator;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.EvolutionNoteService;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.EvolutionNoteValidator;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.UpdateEvolutionNoteService;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.domain.EvolutionNoteBo;

@Slf4j
@RequiredArgsConstructor
@Service
public class UpdateEvolutionNoteServiceImpl implements UpdateEvolutionNoteService {

	private final InternmentDocumentModificationValidator documentModificationValidator;
	private final SharedDocumentPort sharedDocumentPort;
	private final InternmentEpisodeService internmentEpisodeService;
	private final DateTimeProvider dateTimeProvider;
	private final DocumentFactory documentFactory;
	private final EvolutionNoteService evolutionNoteService;
	private final EvolutionNoteValidator evolutionNoteValidator;

	@Override
	@Transactional
	public Long execute(Integer intermentEpisodeId, Long oldEvolutionId, EvolutionNoteBo newEvolution) {
		log.debug("Input parameters -> intermentEpisodeId {}, oldEvolutionId {}, newEvolution {} ", intermentEpisodeId, oldEvolutionId, newEvolution);
		evolutionNoteValidator.validateRolePermission(newEvolution);
		evolutionNoteValidator.assertContextValid(newEvolution);
		EvolutionNoteBo oldEvolution = evolutionNoteService.getDocument(oldEvolutionId);
		newEvolution.setInitialDocumentId(oldEvolution.getInitialDocumentId() != null ? oldEvolution.getInitialDocumentId() : oldEvolution.getId());
		documentModificationValidator.execute(intermentEpisodeId, oldEvolution.getId(), newEvolution.getModificationReason(), EDocumentType.map(newEvolution.getDocumentType()));
		newEvolution.setPerformedDate(dateTimeProvider.nowDateTime());
		evolutionNoteValidator.validateNursePermissionToLoadProcedures(newEvolution);
		evolutionNoteValidator.assertEvolutionNoteValid(newEvolution);
		if (newEvolution.getMainDiagnosis() != null) {
			evolutionNoteValidator.assertDiagnosisValid(newEvolution, newEvolution.getMainDiagnosis());
			if(!oldEvolution.getMainDiagnosis().getSnomedSctid().equals(newEvolution.getMainDiagnosis().getSnomedSctid()))
				newEvolution.getMainDiagnosis().setId(null);
		}
		var internmentEpisode = internmentEpisodeService.getInternmentEpisode(newEvolution.getEncounterId(), newEvolution.getInstitutionId());
		evolutionNoteValidator.assertEffectiveRiskFactorTimeValid(newEvolution, internmentEpisode.getEntryDate());
		evolutionNoteValidator.assertAnthropometricData(newEvolution);


		setOtherDiagnostics(newEvolution, oldEvolution);
		newEvolution.getProcedures().addAll(getDischargedConcepts(newEvolution.getProcedures(), oldEvolution.getProcedures(), ProceduresStatus.ERROR));
		newEvolution.getPersonalHistories().addAll(getDischargedConcepts(newEvolution.getPersonalHistories(), oldEvolution.getPersonalHistories(), ConditionClinicalStatus.INACTIVE)
				.stream().peek(a -> a.setVerificationId(ConditionVerificationStatus.ERROR)).collect(Collectors.toList()));
		newEvolution.getFamilyHistories().addAll(getDischargedConcepts(newEvolution.getFamilyHistories(), oldEvolution.getFamilyHistories(), ConditionClinicalStatus.INACTIVE)
				.stream().peek(a -> a.setVerificationId( ConditionVerificationStatus.ERROR)).collect(Collectors.toList()));
		newEvolution.getAllergies().addAll(getDischargedConcepts(newEvolution.getAllergies(), oldEvolution.getAllergies(), AllergyIntoleranceClinicalStatus.INACTIVE)
				.stream().peek(a -> a.setVerificationId(AllergyIntoleranceVerificationStatus.ERROR)).collect(Collectors.toList()));
		newEvolution.getImmunizations().addAll(getDischargedConcepts(newEvolution.getImmunizations(), oldEvolution.getImmunizations(), InmunizationStatus.ERROR));

		sharedDocumentPort.updateDocumentModificationReason(oldEvolution.getId(), newEvolution.getModificationReason());
		sharedDocumentPort.deleteDocument(oldEvolution.getId(), DocumentStatus.ERROR);

		// create new document
		newEvolution.setPatientInternmentAge(internmentEpisodeService.getEntryDate(newEvolution.getEncounterId()).toLocalDate());
		newEvolution.setId(documentFactory.run(newEvolution, true));
		internmentEpisodeService.addEvolutionNote(newEvolution.getEncounterId(), newEvolution.getId());
		log.debug("Output -> {}", newEvolution.getId());
		return newEvolution.getId();
	}

	private void setOtherDiagnostics(EvolutionNoteBo newEvolution, EvolutionNoteBo oldEvolution) {
		newEvolution.getDiagnosis().forEach(diagnosis -> {
			if (oldEvolution.getDiagnosis().stream().noneMatch(diagnosis::equals)||oldEvolution.getDiagnosis().isEmpty()) diagnosis.setId(null);
		});
	}

	private <T extends ClinicalTerm> List<T> getDischargedConcepts(List<T> newTerms, List<T> oldTerms, String errorCode) {
		if(newTerms.isEmpty() || oldTerms.isEmpty())
			return new ArrayList<>();
		return oldTerms.stream().filter(term -> newTerms.stream().noneMatch(term::equals)).peek(p -> {
			p.setId(null);
			p.setStatusId(errorCode);
		}).collect(Collectors.toList());
	}

}
