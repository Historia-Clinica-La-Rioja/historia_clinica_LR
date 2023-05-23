package net.pladema.establishment.repository.entity;

import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.EqualsAndHashCode;
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

@Table(name = "hierarchical_unit_type")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@ToString
@Where(clause = "deleted=false")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class HierarchicalUnitType extends SGXAuditableEntity<Integer> {

	public static final short DIRECCION = 1;

	public static final short UNIDAD_DIAGNOSTICO_TRATAMIENTO = 2;

	public static final short UNIDAD_INTERNACION = 3;

	public static final short UNIDAD_CONSULTA = 4;

	public static final short UNIDAD_ENFERMERIA = 5;

	public static final short JEFATURA_SALA = 6;

	public static final short DEPARTAMENTO = 7;

	public static final short SERVICIO = 8;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Integer id;

	@Column(name = "description", nullable = false)
	private String description;
}
