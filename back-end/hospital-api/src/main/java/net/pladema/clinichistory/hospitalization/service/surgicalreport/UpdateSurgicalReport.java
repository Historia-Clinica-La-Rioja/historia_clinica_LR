package net.pladema.clinichistory.hospitalization.service.surgicalreport;

import java.util.Optional;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.NoteRepository;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SurgicalReportRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.lamansys.sgh.clinichistory.application.createDocument.DocumentFactory;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.EDocumentType;
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
	private final NoteRepository noteRepository;
	private final SurgicalReportRepository surgicalReportRepository;

	@Transactional
	public Long execute(Integer intermentEpisodeId, Long oldDocumentId, SurgicalReportBo newReport) {
		log.debug("Input parameters -> intermentEpisodeId {}, oldDocumentId {}, newReport {} ", intermentEpisodeId, oldDocumentId, newReport);
		surgicalReportValidator.assertContextValid(newReport);
		surgicalReportValidator.assertProsthesisValid(newReport);
		SurgicalReportBo oldReport = getSurgicalReport.run(oldDocumentId);
		newReport.setInitialDocumentId(oldReport.getInitialDocumentId() != null ? oldReport.getInitialDocumentId() : oldReport.getId());
		newReport.setPerformedDate(dateTimeProvider.nowDateTime());
		documentModificationValidator.execute(intermentEpisodeId, oldReport.getId(), newReport.getModificationReason(), EDocumentType.SURGICAL_HOSPITALIZATION_REPORT);
		surgicalReportValidator.assertSurgicalReportValid(newReport);
		sharedDocumentPort.updateDocumentModificationReason(oldReport.getId(), newReport.getModificationReason());
		sharedDocumentPort.deleteDocument(oldReport.getId(), DocumentStatus.ERROR);
		newReport.setPatientInternmentAge(internmentEpisodeService.getEntryDate(newReport.getEncounterId()).toLocalDate());
		mapToNewConcepts(newReport);
		if (surgicalReportRepository.surgicalReportHasNote(oldDocumentId))
			noteRepository.updateSurgicalReportNoteByDocumentId(oldDocumentId, newReport.getDescription());
		newReport.setId(documentFactory.run(newReport, newReport.isConfirmed()));
		surgicalReportRepository.updateDocumentIdByDocumentId(oldDocumentId, newReport.getId());
		surgicalReportRepository.updateStartDateTimeIdByDocumentId(newReport.getId(), newReport.getStartDateTime());
		surgicalReportRepository.updateEndDateTimeIdByDocumentId(newReport.getId(), newReport.getEndDateTime());
		surgicalReportRepository.updateHasProsthesisByDocumentId(newReport.getId(), newReport.hasProsthesis());
		log.debug("Output -> {}", newReport.getId());
		return newReport.getId();
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
}
