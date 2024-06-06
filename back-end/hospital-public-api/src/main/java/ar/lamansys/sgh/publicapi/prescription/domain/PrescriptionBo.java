package ar.lamansys.sgh.publicapi.prescription.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof PrescriptionBo)) return false;
		PrescriptionBo that = (PrescriptionBo) o;
		return Objects.equals(getDomain(), that.getDomain()) && Objects.equals(getPrescriptionId(), that.getPrescriptionId()) && Objects.equals(getPrescriptionDate(), that.getPrescriptionDate()) && Objects.equals(getDueDate(), that.getDueDate()) && Objects.equals(getLink(), that.getLink()) && Objects.equals(getIsArchived(), that.getIsArchived()) && Objects.equals(getPatientPrescriptionBo(), that.getPatientPrescriptionBo()) && Objects.equals(getInstitutionPrescriptionBo(), that.getInstitutionPrescriptionBo()) && Objects.equals(getProfessionalPrescriptionBo(), that.getProfessionalPrescriptionBo()) && Objects.equals(getPrescriptionsLineBo(), that.getPrescriptionsLineBo());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getDomain(), getPrescriptionId(), getPrescriptionDate(), getDueDate(), getLink(), getIsArchived(), getPatientPrescriptionBo(), getInstitutionPrescriptionBo(), getProfessionalPrescriptionBo(), getPrescriptionsLineBo());
	}
}
