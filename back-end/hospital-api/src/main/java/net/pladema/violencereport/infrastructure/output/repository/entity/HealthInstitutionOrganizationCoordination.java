package net.pladema.violencereport.infrastructure.output.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.violencereport.infrastructure.output.repository.embedded.HealthInstitutionOrganizationCoordinationPK;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "health_institution_organization_coordination")
@Entity
public class HealthInstitutionOrganizationCoordination implements Serializable {

	private static final long serialVersionUID = -5032955681280936180L;

	@EmbeddedId
	private HealthInstitutionOrganizationCoordinationPK pk;

	@Column(name = "other_health_institution_organization", length = 100)
	private String otherHealthInstitutionOrganization;

}
