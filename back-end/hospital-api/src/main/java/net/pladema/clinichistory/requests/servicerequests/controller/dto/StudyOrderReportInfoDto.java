package net.pladema.clinichistory.requests.servicerequests.controller.dto;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto.HCEDocumentDataDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.requests.medicationrequests.controller.dto.DoctorInfoDto;

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
    private String imageId;
    private HCEDocumentDataDto hceDocumentDataDto;
    private String snomed;
    private String healthCondition;
    private String source;
    private Boolean seeStudy = false;
    private Boolean viewReport = false;
    private Integer serviceRequestId;
    private Integer diagnosticReportId;

}
