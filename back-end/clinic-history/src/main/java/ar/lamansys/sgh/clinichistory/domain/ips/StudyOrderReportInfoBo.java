package ar.lamansys.sgh.clinichistory.domain.ips;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StudyOrderReportInfoBo {

    private Boolean status;
    private Integer doctorUserId;
    private LocalDateTime creationDate;
    private String imageId;
    private BigInteger documentId;
    private String snomed;
    private String healthCondition;
    private String fileName;
    private String source;
    private Integer serviceRequestId;
    private Integer diagnosticReportId;
	private Boolean hasActiveAppointment;
    private Boolean isAvailableInPACS;
    private String observationsFromServiceRequest;

}
