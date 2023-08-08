package ar.lamansys.virtualConsultation.domain;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

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

	private Boolean responsibleAvailability;

	private Short priorityId;

	private LocalDateTime creationDateTime;

	private String callId;

}
