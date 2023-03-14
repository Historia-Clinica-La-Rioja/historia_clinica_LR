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
	private PatientPrescriptionBo patientPrescriptionBo;
	private InstitutionPrescriptionBo institutionPrescriptionBo;
	private ProfessionalPrescriptionBo professionalPrescriptionBo;
	private List<PrescriptionLineBo> prescriptionsLineBo;

	public PrescriptionBo(String domain,
						  String prescriptionId,
						  LocalDateTime prescriptionDate,
						  LocalDateTime dueDate,
						  PatientPrescriptionBo patientPrescriptionBo,
						  InstitutionPrescriptionBo institutionPrescriptionBo,
						  ProfessionalPrescriptionBo professionalPrescriptionBo,
						  List<PrescriptionLineBo> prescriptionsLineBo) {
		this.domain = domain;
		this.prescriptionDate = prescriptionDate;
		this.prescriptionId = prescriptionId;
		this.dueDate = dueDate;
		this.patientPrescriptionBo = patientPrescriptionBo;
		this.institutionPrescriptionBo = institutionPrescriptionBo;
		this.professionalPrescriptionBo = professionalPrescriptionBo;
		this.prescriptionsLineBo = prescriptionsLineBo;
	}
}
