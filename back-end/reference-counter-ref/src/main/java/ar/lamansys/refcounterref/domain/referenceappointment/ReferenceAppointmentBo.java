package ar.lamansys.refcounterref.domain.referenceappointment;

import ar.lamansys.refcounterref.domain.reference.ReferenceInstitutionBo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Builder
public class ReferenceAppointmentBo {

	private Integer referenceId;

	private Integer appointmentId;

	private Short appointmentStateId;

	private ReferenceInstitutionBo institution;

	private LocalDateTime date;

	private String phoneNumber;

	private String phonePrefix;

	private String professionalFullName;

	private String email;

	private String authorFullName;

	private LocalDateTime createdOn;

	public ReferenceAppointmentBo(Integer referenceId, Integer appointmentId) {
		this.referenceId = referenceId;
		this.appointmentId = appointmentId;
	}

}
