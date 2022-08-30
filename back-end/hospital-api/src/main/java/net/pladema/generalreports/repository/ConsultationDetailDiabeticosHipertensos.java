package net.pladema.generalreports.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ConsultationDetailDiabeticosHipertensos {

	private String id;

	private String institution;

	private String attentionDate;

	private String lender;

	private String identificationLender;

	private String patient;

	private String identificationPatient;

	private String problem;

	private String reasons;

	private String glycosylatedHemoglobinBloodPressure;

	private String medication;

}
