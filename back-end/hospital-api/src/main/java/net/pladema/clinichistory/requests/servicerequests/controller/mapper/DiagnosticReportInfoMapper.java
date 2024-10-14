package net.pladema.clinichistory.requests.servicerequests.controller.mapper;

import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosticReportBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceRequestDto;

import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.servicerequests.domain.DiagnosticReportResultsBo;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SnomedDto;
import net.pladema.clinichistory.requests.medicationrequests.controller.dto.DoctorInfoDto;
import net.pladema.clinichistory.requests.medicationrequests.controller.dto.HealthConditionInfoDto;
import net.pladema.clinichistory.requests.servicerequests.controller.dto.DiagnosticReportInfoDto;
import net.pladema.patient.controller.dto.PatientMedicalCoverageDto;
import net.pladema.staff.controller.dto.ProfessionalDto;

@Slf4j
@Component
public class DiagnosticReportInfoMapper {

    @Named("parseTo")
    public DiagnosticReportInfoDto parseTo(DiagnosticReportResultsBo resultsBo, ProfessionalDto professionalDto, ReferenceRequestDto referenceRequestDto){
        log.debug("Input parameters -> resultsBo {}, professionalDto {}, referenceRequestDto {}", resultsBo, professionalDto, referenceRequestDto);
        DiagnosticReportInfoDto result = new DiagnosticReportInfoDto();
        result.setId(resultsBo.getId());
        result.setSnomed(SnomedDto.from(resultsBo.getSnomed()));
        result.setHealthCondition(HealthConditionInfoDto.from(resultsBo.getHealthCondition()));
        result.setObservations(resultsBo.getObservations());
        result.setStatusId(resultsBo.getStatusId());
        result.setDoctor(DoctorInfoDto.from(professionalDto));
        result.setServiceRequestId(resultsBo.getEncounterId());
        result.setCreationDate(resultsBo.getEffectiveTime());
		result.setCategory(resultsBo.getCategory());
		result.setSource(resultsBo.getSource());
		result.setSourceId(resultsBo.getSourceId());
		result.setReferenceRequestDto(referenceRequestDto);
		result.setObservationsFromServiceRequest(resultsBo.getObservationsFromServiceRequest());
        log.debug("Output: {}", result);
        return result;
    }

	@Named("parseTo")
	public DiagnosticReportInfoDto parseTo(DiagnosticReportBo diagnosticReportBo, ProfessionalDto professionalDto, PatientMedicalCoverageDto patientMedicalCoverage, ReferenceRequestDto referenceRequestDto){
		log.debug("input -> diagnosticReportBo{},a professionalDto {}", diagnosticReportBo, professionalDto);
		DiagnosticReportInfoDto result = new DiagnosticReportInfoDto();
		result.setId(diagnosticReportBo.getId());
		result.setSnomed(SnomedDto.from(diagnosticReportBo.getSnomed()));
		result.setHealthCondition(HealthConditionInfoDto.from(diagnosticReportBo.getHealthCondition()));
		result.setObservations(diagnosticReportBo.getObservations());
		result.setStatusId(diagnosticReportBo.getStatusId());
		result.setDoctor(DoctorInfoDto.from(professionalDto));
		result.setServiceRequestId(diagnosticReportBo.getEncounterId());
		result.setCreationDate(diagnosticReportBo.getEffectiveTime());
		result.setCategory(diagnosticReportBo.getCategory());
		result.setSource(diagnosticReportBo.getSource());
		result.setSourceId(diagnosticReportBo.getSourceId());
		result.setCoverageDto(patientMedicalCoverage);
		result.setReferenceRequestDto(referenceRequestDto);
		log.debug("Output: {}", result);
		return result;
	}

	@Named("parseTo")
	public DiagnosticReportInfoDto parseTo(DiagnosticReportResultsBo resultsBo, ProfessionalDto professionalDto, PatientMedicalCoverageDto patientMedicalCoverage, ReferenceRequestDto referenceRequestDto){
		log.debug("Input parameters -> resultsBo {}, professionalDto {}, patientMedicalCoverage {}, referenceRequestDto {}", resultsBo, professionalDto, patientMedicalCoverage, referenceRequestDto);
		DiagnosticReportInfoDto result = new DiagnosticReportInfoDto();
		result.setId(resultsBo.getId());
		result.setSnomed(SnomedDto.from(resultsBo.getSnomed()));
		result.setHealthCondition(HealthConditionInfoDto.from(resultsBo.getHealthCondition()));
		result.setObservations(resultsBo.getObservations());
		result.setStatusId(resultsBo.getStatusId());
		result.setDoctor(DoctorInfoDto.from(professionalDto));
		result.setServiceRequestId(resultsBo.getEncounterId());
		result.setCreationDate(resultsBo.getEffectiveTime());
		result.setCategory(resultsBo.getCategory());
		result.setSource(resultsBo.getSource());
		result.setSourceId(resultsBo.getSourceId());
		result.setCoverageDto(patientMedicalCoverage);
		result.setReferenceRequestDto(referenceRequestDto);
		result.setObservationsFromServiceRequest(resultsBo.getObservationsFromServiceRequest());
		log.debug("Output: {}", result);
		return result;
	}
}
