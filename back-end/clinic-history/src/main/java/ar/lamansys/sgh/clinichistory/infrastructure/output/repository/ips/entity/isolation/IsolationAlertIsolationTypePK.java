package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.isolation;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import java.io.Serializable;
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class IsolationAlertIsolationTypePK implements Serializable {

	@Column(name = "isolation_alert_id", nullable = false)
	private Integer isolationAlertId;
	//See EIsolationType
	@Column(name = "isolation_type_id", nullable = false)
	private Short isolationType;
}
