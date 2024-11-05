package ar.lamansys.sgh.publicapi.prescription.domain;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class PrescriptionLineBo {
	private Integer prescriptionLineNumber;
	private String prescriptionLineStatus;
	private PrescriptionProblemBo prescriptionProblemBo;
	private GenericMedicationBo genericMedicationBo;
	private SuggestedCommercialMedicationBo suggestedCommercialMedicationBo;
	private CommercialMedicationBo commercialMedicationBo;
	private Double unitDosis;
	private Double dayDosis;
	private Double duration;
	private String presentation;
	private Short presentationQuantity;
	private Short presentationPackageQuantity;
	private Double quantity;
	private String quantityUnit;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		PrescriptionLineBo that = (PrescriptionLineBo) o;
		return Objects.equals(prescriptionLineNumber, that.prescriptionLineNumber) &&
				Objects.equals(prescriptionLineStatus, that.prescriptionLineStatus) &&
				Objects.equals(prescriptionProblemBo, that.prescriptionProblemBo) &&
				Objects.equals(genericMedicationBo, that.genericMedicationBo) &&
				Objects.equals(commercialMedicationBo, that.commercialMedicationBo) &&
				Objects.equals(unitDosis, that.unitDosis) &&
				Objects.equals(dayDosis, that.dayDosis) &&
				Objects.equals(duration, that.duration) &&
				Objects.equals(presentation, that.presentation) &&
				Objects.equals(presentationPackageQuantity, that.presentationPackageQuantity) &&
				Objects.equals(presentationQuantity, that.presentationQuantity) &&
				Objects.equals(suggestedCommercialMedicationBo, that.suggestedCommercialMedicationBo);
	}

	@Override
	public int hashCode() {
		return Objects.hash(prescriptionLineNumber, prescriptionLineStatus, prescriptionProblemBo, genericMedicationBo, suggestedCommercialMedicationBo, commercialMedicationBo, unitDosis, dayDosis, duration, presentation, presentationQuantity, presentationPackageQuantity, quantity);
	}

}
