package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class CommercialMedicationPresentationPK implements Serializable {

	private static final long serialVersionUID = 6403454827314668375L;

	@Column(name = "commercial_medication_dosage_form_id")
	private Integer commercialMedicationDosageFormId;

	@Column(name = "commercial_medication_dosage_form_unit_id")
	private Integer commercialMedicationDosageFormUnitId;

}
