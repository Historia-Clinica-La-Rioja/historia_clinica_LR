package net.pladema.clinichistory.hospitalization.service.surgicalreport;

import java.util.Optional;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.clinichistory.application.createDocument.DocumentFactory;
import ar.lamansys.sgh.clinichistory.application.notes.NoteService;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SurgicalReportRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.surgicalreport.SurgicalReport;
import ar.lamansys.sgx.shared.security.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.domain.SurgicalReportBo;
import net.pladema.patient.service.domain.PatientMedicalCoverageBo;
import net.pladema.staff.controller.service.HealthcareProfessionalExternalService;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreateSurgicalReport {

	private final SurgicalReportValidator surgicalReportValidator;
	private final InternmentEpisodeService internmentEpisodeService;
	private final DocumentFactory documentFactory;
	private final HealthcareProfessionalExternalService healthcareProfessionalExternalService;
	private final NoteService noteService;
	private final SurgicalReportRepository surgicalReportRepository;

	public void run(SurgicalReportBo surgicalReport) {
		log.debug("Input parameter -> surgicalReport {}", surgicalReport);
		surgicalReport.setPatientInternmentAge(internmentEpisodeService.getEntryDate(surgicalReport.getEncounterId()).toLocalDate());
		surgicalReportValidator.assertContextValid(surgicalReport);
		surgicalReportValidator.assertProsthesisValid(surgicalReport);
		surgicalReportValidator.assertHasAnamnesis(surgicalReport.getEncounterId());
		surgicalReportValidator.assertSurgicalReportValid(surgicalReport);
		Integer doctorId = healthcareProfessionalExternalService.getProfessionalId(UserInfo.getCurrentAuditor());
		Integer patientMedicalCoverageId = internmentEpisodeService.getMedicalCoverage(surgicalReport.getEncounterId())
				.map(PatientMedicalCoverageBo::getId).orElse(null);
		Long documentId = documentFactory.run(surgicalReport, surgicalReport.isConfirmed());
		Long noteId = Optional.ofNullable(surgicalReport.getDescription()).map(noteService::createNote).orElse(null);
		SurgicalReport entity =
				new SurgicalReport(
						null,
						surgicalReport.getPatientId(),
						surgicalReport.getClinicalSpecialtyId(),
						surgicalReport.getInstitutionId(),
						documentId,
						doctorId,
						false,
						patientMedicalCoverageId,
						surgicalReport.getStartDateTime(),
						surgicalReport.getEndDateTime(),
						noteId,
						surgicalReport.hasProsthesis()
				);
		surgicalReportRepository.save(entity);
	}

}
