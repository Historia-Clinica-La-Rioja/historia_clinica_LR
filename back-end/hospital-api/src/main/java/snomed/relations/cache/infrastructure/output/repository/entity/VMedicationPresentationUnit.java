package snomed.relations.cache.infrastructure.output.repository.entity;

import lombok.Getter;
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
@Table(name = "v_medication_presentation_unit", schema = "snomedct")
@Entity
public class VMedicationPresentationUnit implements Serializable {

	private static final long serialVersionUID = 5774998926396887533L;

	@Id
	@Column(name = "sctid")
	private String sctid;

	@Column(name = "presentation_unit_quantity")
	private String presentationUnitQuantity;

}
