package ar.lamansys.sgh.clinichistory.domain.ips;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StudyTranscribedOrderReportInfoBo {
	private Integer serviceRequestId;
	private String imageId;
	private BigInteger documentId;
	private String fileName;
	private String documentStatus;
	private String professionalName;
	private List<DiagnosticReportBo> diagnosticReports;
	private Boolean status;
	private LocalDateTime creationDate;
	private Boolean isAvailableInPACS;
	private Integer appointmentId;
	private LocalDate appointmentDate;
	private LocalTime appointmentHour;
	private String localViewerUrl;
	private Short reportStatusId;
	private String deriveToInstitution;
}
