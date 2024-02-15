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
public class StudyTranscribedOrderReportInfoBo {
	private String imageId;
	private BigInteger documentId;
	private String fileName;
	private String documentStatus;
	private String professionalName;
	private String healthCondition;
	private String snomed;
	private Boolean status;
	private LocalDateTime creationDate;
	private Boolean isAvailableInPACS;
}
