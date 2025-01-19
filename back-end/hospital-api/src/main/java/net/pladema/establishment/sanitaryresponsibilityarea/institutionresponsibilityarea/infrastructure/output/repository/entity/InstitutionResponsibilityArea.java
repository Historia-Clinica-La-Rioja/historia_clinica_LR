package net.pladema.establishment.sanitaryresponsibilityarea.institutionresponsibilityarea.infrastructure.output.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "institution_responsibility_area")
@Entity
public class InstitutionResponsibilityArea implements Serializable {

	private static final long serialVersionUID = 4962929952504540529L;

	@EmbeddedId
	private InstitutionResponsibilityAreaPK pk;

	@Column(name = "latitude")
	private Double latitude;

	@Column(name = "longitude")
	private Double longitude;

}
