package net.pladema.establishment.repository.entity;

import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;


@Table(name = "hierarchical_unit")
@EntityListeners(SGXAuditListener.class)
@Where(clause = "deleted=false")
@Getter
@Setter
@ToString
@Entity
public class HierarchicalUnit extends SGXAuditableEntity<Integer> implements Serializable {

	private static final long serialVersionUID = 6784592510265343742L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "institution_id", nullable = false)
	private Integer institutionId;

	@Column(name = "type_id", nullable = false)
	private Integer typeId;

	@Column(name = "alias", nullable = false, columnDefinition = "TEXT")
	private String alias;

	@Column(name = "hierarchical_unit_id_to_report")
	private Integer hierarchicalUnitIdToReport;

	@Column(name = "clinical_specialty_id")
	private Integer clinicalSpecialtyId;

	@Column(name = "closest_service_id")
	private Integer closestServiceId;

}
