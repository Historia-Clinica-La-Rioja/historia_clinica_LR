package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.isolation;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "isolation_type")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class IsolationType {
	@Id
	@Column(name = "id")
	@EqualsAndHashCode.Include
	private Short id;
	@Column(name = "description", nullable = false)
	private String description;
}

