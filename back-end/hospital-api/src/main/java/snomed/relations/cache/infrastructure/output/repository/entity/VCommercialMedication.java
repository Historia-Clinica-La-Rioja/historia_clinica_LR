package snomed.relations.cache.infrastructure.output.repository.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@Table(name = "v_commercial_medication", schema = "snomedct")
@Entity
public class VCommercialMedication {

	@Id
	@Column(name = "commercial_sctid", nullable = false)
	private String commercialSctid;

	@Column(name = "commercial_pt", nullable = false)
	private String commercialPt;

	@Column(name = "generic_sctid", nullable = false)
	private String genericSctid;

	@Column(name = "generic_pt", nullable = false)
	private String genericPt;

	@Column(name = "presentation_unit", nullable = false)
	private String presentationUnit;

}
