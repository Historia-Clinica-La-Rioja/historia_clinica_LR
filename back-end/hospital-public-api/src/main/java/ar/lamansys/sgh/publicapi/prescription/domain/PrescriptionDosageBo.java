package ar.lamansys.sgh.publicapi.prescription.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PrescriptionDosageBo {

	private Double unitDosis;

	private Double dayDosis;

	private Double duration;

	private String presentation;

	private Short packageQuantity;

	private Double quantity;

	private Integer frequency;

	private String frequencyUnit;

	private Short presentationPackageQuantity;

	private String quantityUnit;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		PrescriptionDosageBo that = (PrescriptionDosageBo) o;
		return Objects.equals(unitDosis, that.unitDosis) &&
				Objects.equals(dayDosis, that.dayDosis) &&
				Objects.equals(duration, that.duration) &&
				Objects.equals(presentation, that.presentation) &&
				Objects.equals(packageQuantity, that.packageQuantity) &&
				Objects.equals(quantity, that.quantity) &&
				Objects.equals(frequency, that.frequency) &&
				Objects.equals(frequencyUnit, that.frequencyUnit) &&
				Objects.equals(presentationPackageQuantity, that.presentationPackageQuantity);
	}

	@Override
	public int hashCode() {
		return Objects.hash(unitDosis, dayDosis, duration, presentation, packageQuantity, quantity, frequency, frequencyUnit, presentationPackageQuantity);
	}

}
