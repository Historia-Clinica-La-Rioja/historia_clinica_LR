package ar.lamansys.sgh.publicapi.prescription.domain;

import lombok.Getter;

@Getter
public class DispensedMedicationBo {
	private final String snomedId;
	private final String commercialName;
	private final String commercialPresentation;
	private final Integer soldUnits;
	private final String brand;
	private final Double price;
	private final Double affiliatePayment;
	private final Double medicalCoveragePayment;

	private final String pharmacyName;

	private final String pharmacistName;

	private final String observations;

	public DispensedMedicationBo(String snomedId,
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
