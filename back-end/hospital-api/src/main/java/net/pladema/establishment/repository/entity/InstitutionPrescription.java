package net.pladema.establishment.repository.entity;

import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "institution_prescription")
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class InstitutionPrescription extends SGXAuditableEntity<Integer> {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Integer id;

	@Column(name = "sisa_code", nullable = false, length = 15)
	private String sisaCode;

	@Column(name = "name", nullable = false, length = 255)
	private String name;

	@Column(name = "province_id", nullable = false)
	private Short provinceId;

	@Column(name = "dependency_id", nullable = false)
	private Short dependencyId;

	@Column(name = "typology", nullable = false)
	private String typology;

}
