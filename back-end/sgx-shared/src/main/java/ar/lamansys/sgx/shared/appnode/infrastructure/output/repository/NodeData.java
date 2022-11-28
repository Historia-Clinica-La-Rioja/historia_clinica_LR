package ar.lamansys.sgx.shared.appnode.infrastructure.output.repository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "app_node_data")
public class NodeData {

	@Id
	@Column(name = "uuid", length = 36)
	private String uuid;

	@Column(name="updated")
	private LocalDateTime updated;

	@Column(name="interval_in_sec")
	private Integer interval;

	@Column(name = "ip", length = 40)
	private String ip;

	@Column(name="hostname", length = 50)
	private String hostname;

	public boolean isOutdated() {
		LocalDateTime dueDateTime = updated.plus(
				interval + 10, // agrego 10 segundos por si está muy atareado
				ChronoUnit.SECONDS
		);
		return dueDateTime.isBefore(LocalDateTime.now());
	}
}
