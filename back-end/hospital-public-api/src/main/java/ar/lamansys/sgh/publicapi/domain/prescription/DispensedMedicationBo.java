package ar.lamansys.sgh.publicapi.domain.prescription;

import lombok.Builder;
import lombok.Getter;

import javax.validation.ConstraintViolationException;

import java.util.Collections;

@Getter
public class DispensedMedicationBo {
	Integer snomedId;
	String commercialName;
	String commercialPresentation;
	Integer soldUnits;
	String brand;
	Double price;
	Double affiliatePayment;
	Double medicalCoveragePayment;

	public DispensedMedicationBo(Integer snomedId,
								 String commercialName,
								 String commercialPresentation,
								 Integer soldUnits,
								 String brand,
								 Double price,
								 Double affiliatePayment,
								 Double medicalCoveragePayment) {

		if(soldUnits <= 0) {
			throw new ConstraintViolationException("Se debe haber dispensado una cantidad de unidades mayor a 0", Collections.emptySet());
		}

		if((affiliatePayment + medicalCoveragePayment) != price ) {
			throw new ConstraintViolationException("La suma de los pagos debe ser igual al precio del medicamento", Collections.emptySet());
		}


		this.snomedId = snomedId;
		this.commercialName = commercialName;
		this.commercialPresentation = commercialPresentation;
		this.soldUnits = soldUnits;
		this.brand = brand;
		this.price = price;
		this.affiliatePayment = affiliatePayment;
		this.medicalCoveragePayment = medicalCoveragePayment;
	}
}
