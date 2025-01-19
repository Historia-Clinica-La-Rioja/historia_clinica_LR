package net.pladema.violencereport.infrastructure.output.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.violencereport.infrastructure.output.repository.embedded.HealthSystemOrganizationCoordinationPK;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "health_system_organization_coordination")
@Entity
public class HealthSystemOrganizationCoordination implements Serializable {

	private static final long serialVersionUID = 72342849706804139L;

	@EmbeddedId
	private HealthSystemOrganizationCoordinationPK pk;

	@Column(name = "other_health_system_organization", length = 100)
	private String otherHealthSystemOrganization;

}
