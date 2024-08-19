package net.pladema.medicalconsultation.shockroom.infrastructure.repository.entity;

import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
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

import java.io.Serializable;

@Entity
@Table(name = "shockroom")
@Getter
@Setter
@ToString
@EntityListeners(SGXAuditListener.class)
public class Shockroom extends SGXAuditableEntity<Integer> implements Serializable {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "institution_id", nullable = false)
	private Integer institutionId;

	@Column(name = "sector_id", nullable = false)
	private Integer sectorId;

	@Column(name = "description", nullable = false)
	private String description;

	@Column(name = "topic")
	private String topic;

}
