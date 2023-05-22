package ar.lamansys.sgh.publicapi.domain.prescription;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@ToString
@Getter
@Setter
public class PrescriptionBo {
	private String domain;
	private String prescriptionId;
	private LocalDateTime prescriptionDate;
	private LocalDateTime dueDate;

	private String link;

	private Boolean isArchived;
	private PatientPrescriptionBo patientPrescriptionBo;
	private InstitutionPrescriptionBo institutionPrescriptionBo;
	private ProfessionalPrescriptionBo professionalPrescriptionBo;
	private List<PrescriptionLineBo> prescriptionsLineBo;

	public PrescriptionBo(String domain,
						  String prescriptionId,
						  LocalDateTime prescriptionDate,
						  LocalDateTime dueDate,
						  String link,
						  Boolean isArchived,
						  PatientPrescriptionBo patientPrescriptionBo,
						  InstitutionPrescriptionBo institutionPrescriptionBo,
						  ProfessionalPrescriptionBo professionalPrescriptionBo,
						  List<PrescriptionLineBo> prescriptionsLineBo) {
		this.domain = domain;
		this.prescriptionDate = prescriptionDate;
		this.prescriptionId = prescriptionId;
		this.dueDate = dueDate;
		this.link = link;
		this.isArchived = isArchived;
		this.patientPrescriptionBo = patientPrescriptionBo;
		this.institutionPrescriptionBo = institutionPrescriptionBo;
		this.professionalPrescriptionBo = professionalPrescriptionBo;
		this.prescriptionsLineBo = prescriptionsLineBo;
	}
}
