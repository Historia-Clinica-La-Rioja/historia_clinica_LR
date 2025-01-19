package net.pladema.establishment.repository.entity;

import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import net.pladema.establishment.controller.dto.InstitutionalGroupRuleDto;

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
@Table(name = "institutional_group_rule")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(SGXAuditListener.class)
@Where(clause = "deleted=false")
public class InstitutionalGroupRule extends SGXAuditableEntity<Integer> implements Serializable {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "rule_id")
	private Integer ruleId;

	@Column(name = "institutional_group_id")
	private Integer institutionalGroupId;

	@Column(name = "regulated")
	private boolean regulated;

	@Column(name = "comment")
	private String comment;

	public InstitutionalGroupRule(InstitutionalGroupRuleDto dto){
		this.id = dto.getId();
		this.ruleId = dto.getRuleId();
		this.institutionalGroupId = dto.getInstitutionalGroupId();
		this.regulated = dto.isRegulated();
		this.comment = dto.getComment();
	}

}