package net.pladema.clinichistory.requests.medicationrequests.infrastructure.output.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.clinichistory.requests.medicationrequests.domain.ValidatedMedicationRequestBo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "validated_medication_request")
@Entity
public class ValidatedMedicationRequest implements Serializable {

	private static final long serialVersionUID = 46496263237339972L;

	@Id
	@Column(name = "medication_request_id")
	private Integer medicationRequestId;

 	@Column(name = "external_medication_request_id", length = 50)
	private String externalMedicationRequestId;

	public ValidatedMedicationRequest(ValidatedMedicationRequestBo validatedMedicationRequestBo) {
		this.medicationRequestId = validatedMedicationRequestBo.getMedicationRequestId();
		this.externalMedicationRequestId = validatedMedicationRequestBo.getExternalMedicationRequestId();
	}

}
