package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.isolation;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "isolation_status")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class IsolationStatus {
	@Id
	@Column(name = "id")
	@EqualsAndHashCode.Include
	private Short id;
	@Column(name = "description", nullable = false)
	private String description;
}
