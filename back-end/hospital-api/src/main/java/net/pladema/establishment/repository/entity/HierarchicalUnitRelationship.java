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

@Table(name = "hierarchical_unit_relationship")
@EntityListeners(SGXAuditListener.class)
@Where(clause = "deleted=false")
@Getter
@Setter
@ToString
@Entity
public class HierarchicalUnitRelationship extends SGXAuditableEntity<Integer> implements Serializable {

	private static final long serialVersionUID = -7469904410551800684L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "hierarchical_unit_parent_id", nullable = false)
	private Integer hierarchicalUnitParentId;

	@Column(name = "hierarchical_unit_child_id", nullable = false)
	private Integer hierarchicalUnitChildId;
}
