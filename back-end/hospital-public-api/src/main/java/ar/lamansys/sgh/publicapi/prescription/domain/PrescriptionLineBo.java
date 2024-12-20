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
	private Short packageQuantity;
	private Short presentationPackageQuantity;
	private Double quantity;
	private String quantityUnit;

	public PrescriptionLineBo(Integer prescriptionLineNumber, String prescriptionLineStatus, PrescriptionProblemBo prescriptionProblemBo,
							  GenericMedicationBo genericMedicationBo, SuggestedCommercialMedicationBo suggestedCommercialMedicationBo,
							  CommercialMedicationBo commercialMedicationBo, Double unitDosis, Double dayDosis, Double duration,
							  String presentation, Short packageQuantity, Short presentationPackageQuantity, Double quantity) {
		this.prescriptionLineNumber = prescriptionLineNumber;
		this.prescriptionLineStatus = prescriptionLineStatus;
		this.prescriptionProblemBo = prescriptionProblemBo;
		this.genericMedicationBo = genericMedicationBo;
		this.suggestedCommercialMedicationBo = suggestedCommercialMedicationBo;
		this.commercialMedicationBo = commercialMedicationBo;
		this.unitDosis = unitDosis;
		this.dayDosis = dayDosis;
		this.duration = duration;
		this.presentation = presentation;
		this.packageQuantity = packageQuantity;
		this.presentationPackageQuantity = presentationPackageQuantity;
		this.quantity = quantity;
	}

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
				Objects.equals(packageQuantity, that.packageQuantity) &&
				Objects.equals(suggestedCommercialMedicationBo, that.suggestedCommercialMedicationBo);
	}

	@Override
	public int hashCode() {
		return Objects.hash(prescriptionLineNumber, prescriptionLineStatus, prescriptionProblemBo, genericMedicationBo, suggestedCommercialMedicationBo, commercialMedicationBo, unitDosis, dayDosis, duration, presentation, packageQuantity, presentationPackageQuantity, quantity);
	}

}
