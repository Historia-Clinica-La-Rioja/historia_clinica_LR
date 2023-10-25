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
    private String imageId;
    private HCEDocumentDataDto hceDocumentDataDto;
    private String healthCondition;
    private String snomed;
    private Boolean status;
    private LocalDateTime creationDate;
    private Boolean seeStudy;
    private Boolean viewReport;
    private DoctorInfoDto doctor;
    private String source;
}
