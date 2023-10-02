package net.pladema.establishment.repository.entity;

import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import net.pladema.establishment.controller.dto.InstitutionalGroupDto;

import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;


@Entity
@Table(name = "institutional_group")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(SGXAuditListener.class)
@Where(clause="deleted=false")
public class InstitutionalGroup extends SGXAuditableEntity<Integer> implements Serializable {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="type_id")
	private Short typeId;

	@Column(name="name")
	private String name;

	public InstitutionalGroup (InstitutionalGroupDto dto){
		this.id = dto.getId();
		this.typeId = dto.getTypeId();
		this.name = dto.getName();
	}

}