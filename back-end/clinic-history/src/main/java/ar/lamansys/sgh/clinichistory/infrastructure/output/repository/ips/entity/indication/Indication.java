package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.indication;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "indication")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
public class Indication extends SGXAuditableEntity<Integer> implements Serializable {
	
	private static final long serialVersionUID = -3053291021636483828L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "patient_id", nullable = false)
	private Integer patientId;

	@Column(name = "type_id", nullable = false)
	private Short typeId;

	@Column(name = "status_id")
	private Short statusId;

	@Column(name = "indication_date", nullable = false)
	private LocalDate indicationDate;

	@Column(name = "professional_id", nullable = false)
	private Integer professionalId;

}
