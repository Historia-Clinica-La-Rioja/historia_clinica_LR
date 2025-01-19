package net.pladema.clinichistory.requests.servicerequests.controller.dto;

import java.time.LocalDateTime;

import javax.annotation.Nullable;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SnomedDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.requests.medicationrequests.controller.dto.DoctorInfoDto;
import net.pladema.clinichistory.requests.medicationrequests.controller.dto.HealthConditionInfoDto;
import net.pladema.patient.controller.dto.PatientMedicalCoverageDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DiagnosticReportInfoDto {
    private Integer id;
    private SnomedDto snomed;
    private HealthConditionInfoDto healthCondition;

    @Nullable
    private String observations;

    @Nullable
    private String link;

    private String statusId;
    private DoctorInfoDto doctor;
    private Integer serviceRequestId;
    private LocalDateTime creationDate;
    private String category;
    private String source;
    private Integer sourceId;

    @Nullable
    private PatientMedicalCoverageDto coverageDto;

    @Nullable
    private ReferenceRequestDto referenceRequestDto;

    @Nullable
    private String observationsFromServiceRequest;

    public DiagnosticReportInfoDto(DiagnosticReportInfoDto diagnosticReportInfoDto) {
        this.snomed = diagnosticReportInfoDto.getSnomed();
        this.healthCondition = diagnosticReportInfoDto.getHealthCondition();
        this.observations = diagnosticReportInfoDto.getObservations();
        this.link = diagnosticReportInfoDto.getLink();
        this.statusId = diagnosticReportInfoDto.getStatusId();
        this.doctor = diagnosticReportInfoDto.getDoctor();
        this.serviceRequestId = diagnosticReportInfoDto.getServiceRequestId();
        this.creationDate = diagnosticReportInfoDto.creationDate;
        this.category = diagnosticReportInfoDto.getCategory();
        this.referenceRequestDto = diagnosticReportInfoDto.getReferenceRequestDto();
        this.observationsFromServiceRequest = diagnosticReportInfoDto.getObservationsFromServiceRequest();
    }
}
