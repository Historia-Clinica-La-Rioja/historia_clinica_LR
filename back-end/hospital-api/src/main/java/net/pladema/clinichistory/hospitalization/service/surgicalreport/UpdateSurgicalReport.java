package net.pladema.clinichistory.hospitalization.service.surgicalreport;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.lamansys.sgh.clinichistory.application.createDocument.DocumentFactory;
import ar.lamansys.sgh.clinichistory.domain.ips.ClinicalTerm;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.EDocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ConditionClinicalStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ProceduresStatus;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedDocumentPort;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.service.InternmentDocumentModificationValidator;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.domain.SurgicalReportBo;

@Service
@Slf4j
@RequiredArgsConstructor
public class UpdateSurgicalReport {

	private final InternmentDocumentModificationValidator documentModificationValidator;
	private final SharedDocumentPort sharedDocumentPort;
	private final SurgicalReportValidator surgicalReportValidator;
	private final InternmentEpisodeService internmentEpisodeService;
	private final DateTimeProvider dateTimeProvider;
	private final DocumentFactory documentFactory;
	private final GetSurgicalReport getSurgicalReport;

	@Transactional
	public Long execute(Integer intermentEpisodeId, Long oldSurgicalReportId, SurgicalReportBo newReport) {
		log.debug("Input parameters -> intermentEpisodeId {}, oldSurgicalReportId {}, newReport {} ", intermentEpisodeId, oldSurgicalReportId, newReport);
		surgicalReportValidator.assertContextValid(newReport);
		SurgicalReportBo oldReport = getSurgicalReport.run(oldSurgicalReportId);
		newReport.setInitialDocumentId(oldReport.getInitialDocumentId() != null ? oldReport.getInitialDocumentId() : oldReport.getId());
		newReport.setPerformedDate(dateTimeProvider.nowDateTime());
		documentModificationValidator.execute(intermentEpisodeId, oldReport.getId(), newReport.getModificationReason(), EDocumentType.SURGICAL_HOSPITALIZATION_REPORT);
		surgicalReportValidator.assertSurgicalReportValid(newReport);
		sharedDocumentPort.updateDocumentModificationReason(oldReport.getId(), newReport.getModificationReason());
		sharedDocumentPort.deleteDocument(oldReport.getId(), DocumentStatus.ERROR);
		newReport.setPatientInternmentAge(internmentEpisodeService.getEntryDate(newReport.getEncounterId()).toLocalDate());

		if (oldReport.isConfirmed()) mapToPosibleDischargedConcepts(newReport, oldReport);
		else mapToNewConcepts(newReport);

		newReport.setId(documentFactory.run(newReport, newReport.isConfirmed()));
		log.debug("Output -> {}", newReport.getId());
		return newReport.getId();
	}

	private void mapToPosibleDischargedConcepts(SurgicalReportBo newReport, SurgicalReportBo oldReport) {
		newReport.getPreoperativeDiagnosis().addAll(getDischargedConcepts(newReport.getPreoperativeDiagnosis(), oldReport.getPreoperativeDiagnosis(), ConditionClinicalStatus.INACTIVE));
		newReport.getPostoperativeDiagnosis().addAll(getDischargedConcepts(newReport.getPostoperativeDiagnosis(), oldReport.getPostoperativeDiagnosis(), ConditionClinicalStatus.INACTIVE));
		newReport.getSurgeryProcedures().addAll(getDischargedConcepts(newReport.getSurgeryProcedures(), oldReport.getSurgeryProcedures(), ProceduresStatus.ERROR));
		newReport.getProcedures().addAll(getDischargedConcepts(newReport.getProcedures(), oldReport.getProcedures(), ProceduresStatus.ERROR));
		newReport.getAnesthesia().addAll(getDischargedConcepts(newReport.getAnesthesia(), oldReport.getAnesthesia(), ProceduresStatus.ERROR));
		Optional.ofNullable(newReport.getHealthcareProfessionals()).ifPresent(list -> list.forEach(d -> d.setId(null)));
		newReport.getCultures().addAll(getDischargedConcepts(newReport.getCultures(), oldReport.getCultures(), ProceduresStatus.ERROR));
		newReport.getFrozenSectionBiopsies().addAll(getDischargedConcepts(newReport.getFrozenSectionBiopsies(), oldReport.getFrozenSectionBiopsies(), ProceduresStatus.ERROR));
		newReport.getDrainages().addAll(getDischargedConcepts(newReport.getDrainages(), oldReport.getDrainages(), ProceduresStatus.ERROR));
	}

	private void mapToNewConcepts(SurgicalReportBo newReport) {
		Optional.ofNullable(newReport.getPreoperativeDiagnosis()).ifPresent(list -> list.forEach(d -> d.setId(null)));
		Optional.ofNullable(newReport.getPostoperativeDiagnosis()).ifPresent(list -> list.forEach(d -> d.setId(null)));
		Optional.ofNullable(newReport.getSurgeryProcedures()).ifPresent(list -> list.forEach(d -> d.setId(null)));
		Optional.ofNullable(newReport.getProcedures()).ifPresent(list -> list.forEach(d -> d.setId(null)));
		Optional.ofNullable(newReport.getAnesthesia()).ifPresent(list -> list.forEach(d -> d.setId(null)));
		Optional.ofNullable(newReport.getCultures()).ifPresent(list -> list.forEach(d -> d.setId(null)));
		Optional.ofNullable(newReport.getFrozenSectionBiopsies()).ifPresent(list -> list.forEach(d -> d.setId(null)));
		Optional.ofNullable(newReport.getDrainages()).ifPresent(list -> list.forEach(d -> d.setId(null)));
		Optional.ofNullable(newReport.getDrainages()).ifPresent(list -> list.forEach(d -> d.setId(null)));
		Optional.ofNullable(newReport.getHealthcareProfessionals()).ifPresent(list -> list.forEach(d -> d.setId(null)));
	}


	private <T extends ClinicalTerm> List<T> getDischargedConcepts(List<T> newTerms, List<T> oldTerms, String errorCode) {
		if (newTerms.isEmpty() || oldTerms.isEmpty()) return new ArrayList<>();
		return oldTerms.stream().filter(term -> newTerms.stream().noneMatch(term::equals)).peek(p -> {
			p.setId(null);
			p.setStatusId(errorCode);
		}).collect(Collectors.toList());
	}


}
