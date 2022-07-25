package net.pladema.medicalconsultation.appointment.controller.dto;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.pladema.medicalconsultation.appointment.repository.domain.AppointmentTicketBo;

@Getter
@Setter
@AllArgsConstructor
public class AppointmentTicketDto {

	private String institution;

	private String dni;

	private String patientFullName;

	private String medicalCoverage;

	private String date;

	private String hour;

	private String doctorsOffice;

	private String doctorFullName;

	public static AppointmentTicketDto toAppointmentTicketDto(AppointmentTicketBo bo) {
		if(bo == null)
			return null;

		DecimalFormat decimalFormat = new DecimalFormat("###,###.##");
		final String notMedicalCoverage = "SIN COBERTURA";
		String medicalCoverage = bo.getMedicalCoverage();

		return new AppointmentTicketDto(
				bo.getInstitution().toUpperCase(Locale.ROOT),
				decimalFormat.format(Integer.parseInt(bo.getDni())).replaceAll(",", "."),
				bo.getPatientFullName().toUpperCase(Locale.ROOT),
				medicalCoverage == null ? notMedicalCoverage : medicalCoverage.toUpperCase(Locale.ROOT),
				bo.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
				bo.getHour().toString(),
				bo.getDoctorsOffice().toUpperCase(Locale.ROOT),
				bo.getDoctorFullName().toUpperCase(Locale.ROOT)
		);
	}

}
