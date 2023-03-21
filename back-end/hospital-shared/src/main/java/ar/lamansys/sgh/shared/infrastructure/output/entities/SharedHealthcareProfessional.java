package ar.lamansys.sgh.shared.infrastructure.output.entities;

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

@Entity
@Table(name = "healthcare_professional")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners(SGXAuditListener.class)
public class SharedHealthcareProfessional extends SGXAuditableEntity<Integer> implements Serializable {

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

	public SharedHealthcareProfessional(Integer personId) {
		this.personId = personId;
	}
}
