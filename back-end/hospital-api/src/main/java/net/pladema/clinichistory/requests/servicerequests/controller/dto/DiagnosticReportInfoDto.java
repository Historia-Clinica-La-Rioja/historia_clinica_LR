package net.pladema.clinichistory.requests.servicerequests.controller.dto;

import lombok.*;
import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.SnomedDto;
import net.pladema.clinichistory.requests.medicationrequests.controller.dto.DoctorInfoDto;
import net.pladema.clinichistory.requests.medicationrequests.controller.dto.HealthConditionInfoDto;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
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
    private long totalDays;

    public DiagnosticReportInfoDto( DiagnosticReportInfoDto diagnosticReportInfoDto) {
        this.snomed = diagnosticReportInfoDto.getSnomed();
        this.healthCondition = diagnosticReportInfoDto.getHealthCondition();
        this.observations = diagnosticReportInfoDto.getObservations();
        this.link = diagnosticReportInfoDto.getLink();
        this.statusId = diagnosticReportInfoDto.getStatusId();
        this.doctor = diagnosticReportInfoDto.getDoctor();
        this.serviceRequestId = diagnosticReportInfoDto.getServiceRequestId();
        this.totalDays = diagnosticReportInfoDto.totalDays;
    }
}
