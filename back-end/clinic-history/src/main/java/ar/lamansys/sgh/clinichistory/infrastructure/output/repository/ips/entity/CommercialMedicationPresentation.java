package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "commercial_medication_presentation")
@Entity
public class CommercialMedicationPresentation implements Serializable {

	private static final long serialVersionUID = 2426890455162962156L;

	@EmbeddedId
	private CommercialMedicationPresentationPK pk;

}
