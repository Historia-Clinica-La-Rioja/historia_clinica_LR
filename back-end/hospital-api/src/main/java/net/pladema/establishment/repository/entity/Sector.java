package net.pladema.establishment.repository.entity;

import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sector")
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@EntityListeners(SGXAuditListener.class)
public class Sector extends SGXAuditableEntity<Integer> {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Integer id;
	
	@Column(name = "description", nullable = false)
	private String description;
	
	@Column(name = "institution_id", nullable = false)
	private Integer institutionId;

	@Column(name = "sector_id")
	private Integer sectorId;

	@Column(name = "sector_type_id")
	private Short sectorTypeId;

	@Column(name = "sector_organization_id")
	private Short sectorOrganizationId;

	@Column(name = "age_group_id")
	private Short ageGroupId;

	@Column(name = "care_type_id")
	private Short careTypeId;

	@Column(name = "hospitalization_type_id")
	private Short hospitalizationTypeId;

	@Column(name = "informer")
	private Boolean informer = false;
}
