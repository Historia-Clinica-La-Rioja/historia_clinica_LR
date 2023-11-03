package ar.lamansys.sgh.clinichistory.domain.ips;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TranscribedOrderReportInfoBo {
	private String imageId;
	private BigInteger documentId;
	private String fileName;
	private String documentStatus;
	private String professionalName;
	private String healthCondition;
	private String snomed;
	private Boolean status;
	private LocalDateTime creationDate;
}
