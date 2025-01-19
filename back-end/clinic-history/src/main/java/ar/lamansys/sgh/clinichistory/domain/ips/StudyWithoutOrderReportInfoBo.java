package ar.lamansys.sgh.clinichistory.domain.ips;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalTime;

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
	private Integer appointmentId;
	private LocalDate appointmentDate;
	private LocalTime appointmentHour;
	private String localViewerUrl;
	private Short reportStatusId;
	private String deriveToInstitution;

}
