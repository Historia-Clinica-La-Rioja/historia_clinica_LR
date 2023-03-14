package net.pladema.clinichistory.hospitalization.service.anamnesis.impl;

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
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.MedicationStatementStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ProceduresStatus;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedDocumentPort;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.service.InternmentDocumentModificationValidator;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.anamnesis.AnamnesisService;
import net.pladema.clinichistory.hospitalization.service.anamnesis.AnamnesisValidator;
import net.pladema.clinichistory.hospitalization.service.anamnesis.UpdateAnamnesisService;
import net.pladema.clinichistory.hospitalization.service.anamnesis.domain.AnamnesisBo;

@Slf4j
@RequiredArgsConstructor
@Service
public class UpdateAnamnesisServiceImpl implements UpdateAnamnesisService {

	private final InternmentDocumentModificationValidator documentModificationValidator;
	private final SharedDocumentPort sharedDocumentPort;
	private final AnamnesisValidator anamnesisValidator;
	private final InternmentEpisodeService internmentEpisodeService;
	private final DateTimeProvider dateTimeProvider;
	private final DocumentFactory documentFactory;
	private final AnamnesisService anamnesisService;

	@Override
	@Transactional
	public Long execute(Integer intermentEpisodeId, Long oldAnamnesisId, AnamnesisBo newAnamnesis) {
		log.debug("Input parameters -> intermentEpisodeId {}, oldAnamnesisId {}, newAnamnesis {} ", intermentEpisodeId, oldAnamnesisId, newAnamnesis);
		anamnesisValidator.assertContextValid(newAnamnesis);
		AnamnesisBo oldAnamnesis = anamnesisService.getDocument(oldAnamnesisId);
		newAnamnesis.setInitialDocumentId(oldAnamnesis.getInitialDocumentId() != null ? oldAnamnesis.getInitialDocumentId() : oldAnamnesis.getId());
		documentModificationValidator.execute(intermentEpisodeId, oldAnamnesis.getId(), newAnamnesis.getModificationReason(), EDocumentType.ANAMNESIS);
		anamnesisValidator.assertAnamnesisValid(newAnamnesis);
		var internmentEpisode = internmentEpisodeService.getInternmentEpisode(newAnamnesis.getEncounterId(), newAnamnesis.getInstitutionId());
		newAnamnesis.setPerformedDate(dateTimeProvider.nowDateTime());
		anamnesisValidator.assertEffectiveRiskFactorTimeValid(newAnamnesis, internmentEpisode.getEntryDate());
		anamnesisValidator.assertAnthropometricData(newAnamnesis);

		if(!newAnamnesis.getMainDiagnosis().getSnomedSctid().equals(oldAnamnesis.getMainDiagnosis().getSnomedSctid()))
			newAnamnesis.getMainDiagnosis().setId(null);
		setOtherDiagnostics(newAnamnesis, oldAnamnesis);
		newAnamnesis.getProcedures().addAll(getDischargedConcepts(newAnamnesis.getProcedures(), oldAnamnesis.getProcedures(), ProceduresStatus.ERROR));
		newAnamnesis.getPersonalHistories().addAll(getDischargedConcepts(newAnamnesis.getPersonalHistories(), oldAnamnesis.getPersonalHistories(), ConditionClinicalStatus.INACTIVE)
				.stream().peek(a -> a.setVerificationId(ConditionVerificationStatus.ERROR)).collect(Collectors.toList()));
		newAnamnesis.getFamilyHistories().addAll(getDischargedConcepts(newAnamnesis.getFamilyHistories(), oldAnamnesis.getFamilyHistories(), ConditionClinicalStatus.INACTIVE)
				.stream().peek(a -> a.setVerificationId(ConditionVerificationStatus.ERROR)).collect(Collectors.toList()));
		newAnamnesis.getAllergies().addAll(getDischargedConcepts(newAnamnesis.getAllergies(), oldAnamnesis.getAllergies(), AllergyIntoleranceClinicalStatus.INACTIVE)
				.stream().peek(a -> a.setVerificationId(AllergyIntoleranceVerificationStatus.ERROR)).collect(Collectors.toList()));
		newAnamnesis.getImmunizations().addAll(getDischargedConcepts(newAnamnesis.getImmunizations(), oldAnamnesis.getImmunizations(), InmunizationStatus.ERROR));
		newAnamnesis.getMedications().addAll(getDischargedConcepts(newAnamnesis.getMedications(), oldAnamnesis.getMedications(), MedicationStatementStatus.ERROR));

		sharedDocumentPort.updateDocumentModificationReason(oldAnamnesis.getId(), newAnamnesis.getModificationReason());
		sharedDocumentPort.deleteDocument(oldAnamnesis.getId(), DocumentStatus.ERROR);
		// create new document
		newAnamnesis.setPatientInternmentAge(internmentEpisodeService.getEntryDate(newAnamnesis.getEncounterId()).toLocalDate());
		newAnamnesis.setId(documentFactory.run(newAnamnesis, true));
		internmentEpisodeService.updateAnamnesisDocumentId(newAnamnesis.getEncounterId(), newAnamnesis.getId());
		log.debug("Output -> {}", newAnamnesis.getId());
		return newAnamnesis.getId();
	}

	private void setOtherDiagnostics(AnamnesisBo newAnamnesis, AnamnesisBo oldAnamnesis) {
		newAnamnesis.getDiagnosis().forEach(diagnosis -> {
			if (oldAnamnesis.getDiagnosis().stream().noneMatch(diagnosis::equals)||oldAnamnesis.getDiagnosis().isEmpty()) diagnosis.setId(null);
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
