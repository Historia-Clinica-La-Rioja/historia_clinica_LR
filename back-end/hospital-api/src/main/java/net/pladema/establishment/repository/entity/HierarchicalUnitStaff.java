package net.pladema.establishment.repository.entity;

import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

@Table(name = "hierarchical_unit_staff")
@EntityListeners(SGXAuditListener.class)
@Where(clause = "deleted=false")
@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
public class HierarchicalUnitStaff extends SGXAuditableEntity<Integer> implements Serializable {

	private static final long serialVersionUID = 5836795131051662964L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "hierarchical_unit_id", nullable = false)
	private Integer hierarchicalUnitId;

	@Column(name = "user_id", nullable = false)
	private Integer userId;

	@Column(name = "responsible")
	private boolean responsible;

}