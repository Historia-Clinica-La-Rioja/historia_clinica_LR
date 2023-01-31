package ar.lamansys.sgh.publicapi.domain.prescription;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.Column;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotNull;

import java.util.Collections;

@Getter
public class DispensedMedicationBo {
	private Integer snomedId;
	private String commercialName;
	private String commercialPresentation;
	private Integer soldUnits;
	private String brand;
	private Double price;
	private Double affiliatePayment;
	private Double medicalCoveragePayment;

	private String pharmacyName;

	private String pharmacistName;

	private String observations;

	public DispensedMedicationBo(Integer snomedId,
								 String commercialName,
								 String commercialPresentation,
								 Integer soldUnits,
								 String brand,
								 Double price,
								 Double affiliatePayment,
								 Double medicalCoveragePayment,
								 String pharmacyName,
								 String pharmacistName,
								 String observations) {
		this.snomedId = snomedId;
		this.commercialName = commercialName;
		this.commercialPresentation = commercialPresentation;
		this.soldUnits = soldUnits;
		this.brand = brand;
		this.price = price;
		this.affiliatePayment = affiliatePayment;
		this.medicalCoveragePayment = medicalCoveragePayment;
		this.pharmacistName = pharmacistName;
		this.pharmacyName = pharmacyName;
		this.observations = observations;
	}
}
