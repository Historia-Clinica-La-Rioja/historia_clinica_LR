package ar.lamansys.sgh.publicapi.domain.prescription;

import ar.lamansys.sgh.publicapi.domain.exceptions.PrescriptionBoEnumException;
import ar.lamansys.sgh.publicapi.domain.exceptions.PrescriptionBoException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@ToString
@Getter
@Setter
public class PrescriptionBo {
	String domain;
	Integer prescriptionId;
	LocalDateTime prescriptionDate;
	LocalDateTime dueDate;
	PatientPrescriptionBo patientPrescriptionBo;
	InstitutionPrescriptionBo institutionPrescriptionBo;
	ProfessionalPrescriptionBo professionalPrescriptionBo;
	List<PrescriptionLineBo> prescriptionsLineBo;

	public PrescriptionBo(String domain,
						  Integer prescriptionId,
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
