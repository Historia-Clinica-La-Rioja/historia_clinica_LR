package net.pladema.clinichistory.requests.servicerequests.controller.dto;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto.HCEDocumentDataDto;
import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import ar.lamansys.sgx.shared.dates.controller.dto.TimeDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.requests.medicationrequests.controller.dto.DoctorInfoDto;

import javax.annotation.Nullable;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class StudyOrderReportInfoDto {

    private Boolean status;
    private DoctorInfoDto doctor;
    private LocalDateTime creationDate;

    @Nullable
    private String imageId;

    @Nullable
    private HCEDocumentDataDto hceDocumentDataDto;

    private String snomed;
    private String healthCondition;
    private String source;
    private Boolean isAvailableInPACS;
    private Boolean viewReport;
    private Integer serviceRequestId;
    private Integer diagnosticReportId;
	private Boolean hasActiveAppointment;
    private String observationsFromServiceRequest;
	private Short reportStatus;
    @Nullable
    private DateDto appointmentDate;
    @Nullable
    private TimeDto appointmentHour;
    @Nullable
    private String localViewerUrl;
	@Nullable
	private String DeriveTo;
}
