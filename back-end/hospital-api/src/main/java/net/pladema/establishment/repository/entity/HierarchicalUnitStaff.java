package net.pladema.establishment.repository.entity;

import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


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
@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class HierarchicalUnitStaff extends SGXAuditableEntity<Integer> implements Serializable {

	private static final long serialVersionUID = 5836795131051662964L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Exclude
	private Integer id;

	@Column(name = "hierarchical_unit_id", nullable = false)
	@EqualsAndHashCode.Include
	private Integer hierarchicalUnitId;

	@Column(name = "user_id", nullable = false)
	@EqualsAndHashCode.Include
	private Integer userId;

	@EqualsAndHashCode.Exclude
	@Column(name = "responsible")
	private boolean responsible;

	public HierarchicalUnitStaff(Integer hierarchicalUnitId, Integer userId, boolean responsible) {
		this.hierarchicalUnitId = hierarchicalUnitId;
		this.userId = userId;
		this.responsible = responsible;
	}

}