package net.pladema.clinichistory.hospitalization.controller.documents.surgicalreport;

import java.time.LocalDateTime;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.controller.constraints.DocumentValid;
import net.pladema.clinichistory.hospitalization.controller.constraints.InternmentValid;
import net.pladema.clinichistory.hospitalization.controller.documents.surgicalreport.dto.SurgicalReportDto;
import net.pladema.clinichistory.hospitalization.controller.documents.surgicalreport.mapper.SurgicalReportMapper;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.domain.SurgicalReportBo;
import net.pladema.clinichistory.hospitalization.service.surgicalreport.CreateSurgicalReport;
import net.pladema.clinichistory.hospitalization.service.surgicalreport.DeleteSurgicalReport;
import net.pladema.clinichistory.hospitalization.service.surgicalreport.GetSurgicalReport;
import net.pladema.clinichistory.hospitalization.service.surgicalreport.UpdateSurgicalReport;
import net.pladema.patient.controller.service.PatientExternalService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/institutions/{institutionId}/internments/{internmentEpisodeId}/surgical-report")
@Validated
@Slf4j
@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
public class SurgicalHospitalizationReportController {

	public static final String OUTPUT = "Output -> {}";

	private final SurgicalReportMapper surgicalReportMapper;

	private final InternmentEpisodeService internmentEpisodeService;

	private final PatientExternalService patientExternalService;

	private final CreateSurgicalReport createSurgicalReport;

	private final GetSurgicalReport getSurgicalReport;

	private final DateTimeProvider dateTimeProvider;

	private final DeleteSurgicalReport deleteSurgicalReport;

	private final UpdateSurgicalReport updateSurgicalReport;

	@PostMapping
	public ResponseEntity<Boolean> create(@PathVariable(name = "institutionId") Integer institutionId, @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId, @RequestBody SurgicalReportDto surgicalReportDto) {
		log.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, surgicalReport {}", institutionId, internmentEpisodeId, surgicalReportDto);
		SurgicalReportBo surgicalReport = surgicalReportMapper.fromSurgicalReportDto(surgicalReportDto);
		setEncounterInformation(internmentEpisodeId, institutionId, surgicalReport);
		createSurgicalReport.run(surgicalReport);
		log.debug(OUTPUT, Boolean.TRUE);
		return ResponseEntity.ok().body(Boolean.TRUE);
	}

	@GetMapping("/{surgicalReportId}")
	@InternmentValid
	@DocumentValid(isConfirmed = true, documentType = DocumentType.SURGICAL_HOSPITALIZATION_REPORT)
	public ResponseEntity<SurgicalReportDto> getById(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
			@PathVariable(name = "surgicalReportId") Long surgicalReportId){
		log.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, surgicalReportId {}",
				institutionId, internmentEpisodeId, surgicalReportId);
		SurgicalReportBo surgicalReport = getSurgicalReport.run(surgicalReportId);
		SurgicalReportDto result = surgicalReportMapper.fromSurgicalReportBo(surgicalReport);
		log.debug(OUTPUT, result);
		return  ResponseEntity.ok().body(result);
	}

	@DeleteMapping("/{surgicalReportId}")
	public ResponseEntity<Boolean> delete(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
			@PathVariable(name = "surgicalReportId") Long surgicalReportId,
			@RequestBody String reason) {
		log.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, surgicalReportId {}, reason {}",
				institutionId, internmentEpisodeId, surgicalReportId, reason);
		deleteSurgicalReport.run(internmentEpisodeId, surgicalReportId, reason);
		log.debug(OUTPUT, Boolean.TRUE);
		return  ResponseEntity.ok().body(Boolean.TRUE);
	}

	@PutMapping("/{surgicalReportId}")
	public ResponseEntity<Long> update(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
			@PathVariable(name = "surgicalReportId") Long surgicalReportId,
			@RequestBody SurgicalReportDto surgicalReportDto) {
		log.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, surgicalReportId {}, surgicalReportDto {}",
				institutionId, internmentEpisodeId, surgicalReportId, surgicalReportDto);
		SurgicalReportBo newReport = surgicalReportMapper.fromSurgicalReportDto(surgicalReportDto);
		internmentEpisodeService.getPatient(internmentEpisodeId)
				.map(patientExternalService::getBasicDataFromPatient)
				.map(patientDto -> new PatientInfoBo(patientDto.getId(), patientDto.getPerson().getGender().getId(), patientDto.getPerson().getAge()))
				.ifPresentOrElse(newReport::setPatientInfo, () -> new NotFoundException("El paciente no existe", "El paciente no existe"));
		newReport.setPatientId(newReport.getPatientInfo().getId());
		newReport.setInstitutionId(institutionId);
		newReport.setEncounterId(internmentEpisodeId);
		Long result = updateSurgicalReport.execute(internmentEpisodeId, surgicalReportId, newReport);
		log.debug(OUTPUT, result);
		return  ResponseEntity.ok().body(result);
	}

	private void setEncounterInformation(Integer encounterId, Integer institutionId, SurgicalReportBo surgicalReport) {
		internmentEpisodeService.getPatient(encounterId).map(patientExternalService::getBasicDataFromPatient).map(patientDto -> new PatientInfoBo(patientDto.getId(), patientDto.getPerson().getGender().getId(), patientDto.getPerson().getAge())).ifPresentOrElse(patientInfo -> {
			surgicalReport.setPatientInfo(patientInfo);
			surgicalReport.setPatientId(patientInfo.getId());
		}, () -> new NotFoundException("El paciente no existe", "El paciente no existe"));
		surgicalReport.setEncounterId(encounterId);
		surgicalReport.setInstitutionId(institutionId);
		LocalDateTime now = dateTimeProvider.nowDateTime();
		surgicalReport.setPerformedDate(now);
	}
}