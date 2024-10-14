package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "medication_statement_commercial_prescription")
@Entity
public class MedicationStatementCommercialPrescription implements Serializable {

	private static final long serialVersionUID = -5179713414228451871L;

	@Id
	@Column(name = "medication_statement_id")
	private Integer medicationStatementId;

	@Column(name = "presentation_unit_quantity", nullable = false)
	private Short presentationUnitQuantity;

	@Column(name = "medication_pack_quantity", nullable = false)
	private Short medicationPackQuantity;

}
