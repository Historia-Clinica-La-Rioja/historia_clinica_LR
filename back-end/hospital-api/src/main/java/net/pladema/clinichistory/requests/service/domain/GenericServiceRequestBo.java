package net.pladema.clinichistory.requests.service.domain;

import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosticReportBo;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GenericServiceRequestBo {

	private PatientInfoBo patientInfo;

	private String categoryId;

	private Integer institutionId;

	private Integer doctorId;

	private Long noteId;

	private List<DiagnosticReportBo> diagnosticReports;

	private LocalDateTime requestDate = LocalDateTime.now();

	private String observations;

	private Short studyTypeId;

	private Boolean requiresTransfer;

	private LocalDateTime deferredDate;

	private LocalDateMapper localDateMapper;

	private static final int maxDeferredDate = 45;

	public Integer getPatientId() {
		if (patientInfo != null)
			return patientInfo.getId();
		return null;
	}

	public LocalDateTime validateDeferredDate(LocalDateTime deferredDate){
		boolean expired = isExpiredRegister(deferredDate.toLocalDate(), deferredDate.toLocalTime());
		if (expired) {
			throw new IllegalArgumentException("Deferred date is expired");
		}
		else if (deferredDate.isAfter(requestDate.plusDays(maxDeferredDate))) {
			throw new IllegalArgumentException("Deferred date is after request date");
		}
		return deferredDate;
	}

	public boolean isExpiredRegister(LocalDate date, LocalTime hour) {
		return LocalDateTime.of(date, hour).isBefore(this.requestDate.minusHours(3));
	}

}
