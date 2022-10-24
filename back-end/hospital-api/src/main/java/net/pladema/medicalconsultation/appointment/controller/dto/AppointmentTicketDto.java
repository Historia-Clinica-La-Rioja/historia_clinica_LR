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

	private String documentNumber;

	private String patientFullName;

	private String medicalCoverage;

	private String date;

	private String hour;

	private String doctorsOffice;

	private String doctorFullName;

	public AppointmentTicketDto(AppointmentTicketBo bo) {
		DecimalFormat decimalFormat = new DecimalFormat("###,###.##");
		final String notMedicalCoverage = "SIN COBERTURA";
		String medicalCoverage;
		if(bo.getMedicalCoverageAcronym() != null && !bo.getMedicalCoverageAcronym().isBlank())
			medicalCoverage = bo.getMedicalCoverageAcronym();
		else medicalCoverage = bo.getMedicalCoverage();

		institution = bo.getInstitution().toUpperCase(Locale.ROOT);
		documentNumber = decimalFormat.format(Integer.parseInt(bo.getDocumentNumber())).replaceAll(",", ".");
		patientFullName = bo.getPatientFullName().toUpperCase(Locale.ROOT);
		this.medicalCoverage = medicalCoverage == null ? notMedicalCoverage : medicalCoverage.toUpperCase(Locale.ROOT);
		date = bo.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		hour = bo.getHour().toString();
		doctorsOffice = bo.getDoctorsOffice().toUpperCase(Locale.ROOT);
		doctorFullName = bo.getDoctorFullName().toUpperCase(Locale.ROOT);
	}

}
