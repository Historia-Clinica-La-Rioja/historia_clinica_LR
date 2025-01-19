package net.pladema.establishment.repository.entity;

import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.establishment.controller.dto.RuleDto;

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
@Table(name = "rule")
@Getter
@Setter
@ToString
@NoArgsConstructor
@EntityListeners(SGXAuditListener.class)
@Where(clause = "deleted=false")
public class Rule extends SGXAuditableEntity<Integer> implements Serializable {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "clinical_specialty_id")
	private Integer clinicalSpecialtyId;

	@Column(name = "snomed_id")
	private Integer snomedId;

	@Column(name = "level")
	private Short level;

	public Rule (RuleDto dto){
		this.id = dto.getId();
		this.clinicalSpecialtyId = dto.getClinicalSpecialtyId();
		this.snomedId = dto.getSnomedId();
		this.level = dto.getLevel();
	}

}
