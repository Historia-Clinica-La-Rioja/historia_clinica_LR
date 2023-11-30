package ar.lamansys.sgh.clinichistory.domain.ips;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigInteger;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StudyWithoutOrderReportInfoBo {
	private String imageId;
	private BigInteger documentId;
	private String documentStatus;
	private String fileName;
	private Boolean status;
	private Boolean isAvailableInPACS;
}
