package ar.lamansys.sgh.publicapi.infrastructure.output;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;

import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "medication_statement_commercial")
@EntityListeners(SGXAuditListener.class)
@Getter
@ToString
@NoArgsConstructor
public class MedicationStatementCommercial extends SGXAuditableEntity<Integer> {

	@Id
	@Column(name = "id")
	private Integer id;

	@Column(name = "snomed_id")
	private Integer snomedId;

	@Column(name = "commercial_name")
	private String commercialName;

	@Column(name = "commercial_presentation")
	private String commercialPresentation;

	@Column(name = "presentation_quantity")
	private Integer presentationQuantity;

	@Column(name = "brand")
	private String brand;

	@Column(name = "price")
	private Double price;

	@Column(name = "affiliate_payment")
	private Double affiliate_payment;

	@Column(name = "medical_coverage_payment")
	private Double medical_coverage;

	@Column(name = "pharmacy_name")
	private String pharmacyName;

	@Column(name = "pharmacist_name")
	private String pharmacistName;

	@Column(name = "observations")
	private String observations;

	public MedicationStatementCommercial(Integer id,
										 Integer snomedId,
										 String commercialName,
										 String commercialPresentation,
										 Integer presentationQuantity,
										 String brand,
										 Double price,
										 Double affiliatePayment,
										 Double medicalCoverage,
										 String pharmacyName,
										 String pharmacistName,
										 String observations) {
		this.id = id;
		this.snomedId = snomedId;
		this.commercialName = commercialName;
		this.commercialPresentation = commercialPresentation;
		this.presentationQuantity = presentationQuantity;
		this.brand = brand;
		this.price = price;
		this.affiliate_payment = affiliatePayment;
		this.medical_coverage = medicalCoverage;
		this.pharmacistName = pharmacistName;
		this.pharmacyName = pharmacyName;
		this.observations = observations;
		this.initializeAuditableFields();
	}
}
