package net.pladema.medicalconsultation.appointment.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class EmptyAppointmentDto {

	private String date;
	private Integer diaryId;
	private String hour;
	private Integer openingHoursId;
	private boolean overturnMode;
	private Integer patientId;
	private String doctorsOfficeDescription;
	private String clinicalSpecialtyName;
	private String alias;
	private String doctorFirstName;
	private String doctorLastName;

}
