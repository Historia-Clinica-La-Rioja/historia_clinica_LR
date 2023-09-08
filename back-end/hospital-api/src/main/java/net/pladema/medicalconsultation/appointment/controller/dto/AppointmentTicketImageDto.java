package net.pladema.medicalconsultation.appointment.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.pladema.medicalconsultation.appointment.repository.domain.AppointmentTicketImageBo;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Getter
@Setter
@AllArgsConstructor
public class AppointmentTicketImageDto {

	private String institution;

	private String documentNumber;

	private String patientFullName;

	private String medicalCoverage;

	private String date;

	private String hour;

	private String sectorName;

	private String studyDescription;

	public AppointmentTicketImageDto(AppointmentTicketImageBo bo) {
		DecimalFormat decimalFormat = new DecimalFormat("###,###.##");
		final String notMedicalCoverage = "SIN COBERTURA";
		final String withOutOrder = "ESTUDIO DE DIAGNÓSTICO POR IMÁGENES";
		String medicalCoverage;
		if(bo.getMedicalCoverageAcronym() != null && !bo.getMedicalCoverageAcronym().isBlank())
			medicalCoverage = bo.getMedicalCoverageAcronym();
		else medicalCoverage = bo.getMedicalCoverage();

		institution = bo.getInstitution().toUpperCase(Locale.ROOT);
		if (bo.getDocumentNumber() != null)
			documentNumber = decimalFormat.format(Integer.parseInt(bo.getDocumentNumber())).replaceAll(",", ".");
		else
			documentNumber = null;
		studyDescription = bo.getStudyDescription() != null ? bo.getStudyDescription().toUpperCase(Locale.ROOT) : withOutOrder;
		patientFullName = bo.getPatientFullName().toUpperCase(Locale.ROOT);
		this.medicalCoverage = medicalCoverage == null ? notMedicalCoverage : medicalCoverage.toUpperCase(Locale.ROOT);
		date = bo.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		hour = bo.getHour().toString();
		sectorName = bo.getSectorName().toUpperCase(Locale.ROOT);
	}

}
