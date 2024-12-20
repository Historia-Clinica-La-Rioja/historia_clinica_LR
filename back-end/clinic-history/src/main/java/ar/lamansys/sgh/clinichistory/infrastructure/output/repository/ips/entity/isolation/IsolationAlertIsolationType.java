package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.isolation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "isolation_alert_isolation_type")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class IsolationAlertIsolationType {
	@EmbeddedId
	private IsolationAlertIsolationTypePK pk;

	public IsolationAlertIsolationType(Integer id, Short typeId) {
		this.setPk(new IsolationAlertIsolationTypePK(id, typeId));
	}
}
