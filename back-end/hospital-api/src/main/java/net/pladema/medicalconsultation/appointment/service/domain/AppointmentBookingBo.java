package net.pladema.medicalconsultation.appointment.service.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.medicalconsultation.appointment.repository.domain.AppointmentBookingVo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class AppointmentBookingBo {

	private String professionalName;

	private LocalDate date;

	private LocalTime hour;

	private String office;

	private String clinicalSpecialtyName;

	public AppointmentBookingBo(AppointmentBookingVo appointmentBookingVo) {
		setRespectiveProfessionalName(appointmentBookingVo.getProfessionalFirstName(), appointmentBookingVo.getProfessionalMiddleNames(), appointmentBookingVo.getProfessionalLastName(), appointmentBookingVo.getProfessionalOtherLastNames());
		this.date = appointmentBookingVo.getDate();
		this.hour = appointmentBookingVo.getHour();
		this.office = appointmentBookingVo.getOffice();
		this.clinicalSpecialtyName = appointmentBookingVo.getClinicalSpecialtyName();
	}

	public void setRespectiveProfessionalName(String firstName, String middleNames, String lastName, String otherLastNames) {
		String fullName = lastName;
		if(!(otherLastNames == null || otherLastNames.isBlank()))
			fullName += " " + otherLastNames;
		if(!(middleNames == null || middleNames.isBlank()))
				fullName = middleNames + " " + fullName;
			fullName = firstName + " " + fullName;
		this.professionalName = fullName;
	}
}
