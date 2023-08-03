package ar.lamansys.virtualConsultation.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class VirtualConsultationBo {

	private Integer id;

	private Integer patientId;

	private String patientName;

	private String patientSelfPerceivedName;

	private String patientLastName;

	private Integer patientAge;

	private String patientGender;

	private String problem;

	private String motive;

	private String clinicalSpecialty;

	private String careLine;

	private Integer institutionId;

	private String institutionName;

	private Short statusId;

	private String responsibleFirstName;

	private String responsibleLastName;

	private Integer responsibleHealthcareProfessionalId;

	private Short priorityId;

	private LocalDateTime creationDateTime;

	private String callId;

}
