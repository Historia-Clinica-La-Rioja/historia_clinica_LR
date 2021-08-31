package net.pladema.staff.repository.entity;

import java.io.Serializable;

import javax.persistence.*;

import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "healthcare_professional")
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners(SGXAuditListener.class)
public class HealthcareProfessional extends SGXAuditableEntity<Integer> implements Serializable {

	private static final long serialVersionUID = 7788785116152807155L;
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Integer id;

	@Column(name = "license_number", nullable = false, length = 512)
	private String licenseNumber;

	@Column(name = "person_id", nullable = false, unique = true)
	private Integer personId;

}
