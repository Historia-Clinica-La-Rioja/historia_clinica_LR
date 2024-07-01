package ar.lamansys.sgh.publicapi.patient.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AppointmentDataBo {
	private Integer id;
	private InstitutionBo institution;
	private DoctorBo doctor;
	private AppointmentMedicalCoverageBo appointmentMedicalCoverage;
	private AppointmentStatusBo appointmentStatus;
	private AppointmentCancellationBo appointmentCancellation;
	private String videocallLink;
	private EncounterModeBo encounterMode;
	private Boolean isOverturn;
	private Boolean isOnline;



	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		AppointmentDataBo that = (AppointmentDataBo) o;
		return id.equals(that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	private LocalDate day;
	private LocalTime hour;
}
