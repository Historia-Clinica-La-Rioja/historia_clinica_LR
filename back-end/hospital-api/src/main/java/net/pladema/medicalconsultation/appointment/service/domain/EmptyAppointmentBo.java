package net.pladema.medicalconsultation.appointment.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class EmptyAppointmentBo {

	private LocalDate date;
	private Integer diaryId;
	private LocalTime hour;
	private Integer openingHoursId;
	private boolean overturnMode;
	private Integer patientId;
	private String doctorsOfficeDescription;
	private String clinicalSpecialtyName;
	private String alias;
	private String doctorFirstName;
	private String doctorLastName;

}
