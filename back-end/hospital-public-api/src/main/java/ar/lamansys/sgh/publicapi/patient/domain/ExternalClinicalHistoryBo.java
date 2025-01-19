package ar.lamansys.sgh.publicapi.patient.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@ToString
@Getter
@Builder
public class ExternalClinicalHistoryBo {

	private Short patientGender;

	private Short patientDocumentType;

	private String patientDocumentNumber;

	private String notes;

	private LocalDate consultationDate;

	private String institution;

	private String professionalName;

	private String professionalSpecialty;

}
