package ar.lamansys.sgx.shared.scheduling.infrastructure.output.repository.shedlock;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "shedlock")
@Getter
@Setter
public class ShedLock implements Serializable {

	private static final long serialVersionUID = 6122710448144131651L;

	@Id
	@Column(name = "name")
	private String name;

	@Column(name = "lock_until ")
	private Timestamp lockUntil;

	@Column(name = "locked_at")
	private Timestamp lockedAt;

	@Column(name = "locked_by")
	private String lockedBy;
}
