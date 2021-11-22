package ar.lamansys.sgx.shared.scheduling.infrastructure.output.repository.synchronization;

import ar.lamansys.sgx.shared.scheduling.infrastructure.output.service.AbstractSync;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
public abstract class AbstractSyncEntity<I> implements AbstractSync<I> {

	@Column(name = "synchronized_date")
	protected LocalDateTime synchronizedDate;

	@Column(name = "priority")
	protected Integer priority;

	@Column(name = "external_id")
	protected String externalId;

	@Column(name = "status_code")
	protected Integer statusCode;

	protected AbstractSyncEntity(LocalDateTime synchronizedDate, Integer priority, String externalId, Integer statusCode) {
		this.synchronizedDate = synchronizedDate;
		this.priority = priority;
		this.externalId = externalId;
		this.statusCode = statusCode;
	}

	@Override
	public void updateErrorStatus(Integer statusCode) {
		setStatusCode(statusCode);
		setSynchronizedDate(LocalDateTime.now());
		setPriority(0);
	}

	@Override
	public void updateOkStatus(String externalId) {
		setExternalId(externalId);
		setStatusCode(null);
		setSynchronizedDate(LocalDateTime.now());
		setPriority(0);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("AbstractSyncEntity{");
		sb.append("id=").append(getId());
		sb.append(", synchronizedDate=").append(synchronizedDate);
		sb.append(", priority=").append(priority);
		sb.append(", externalId='").append(externalId).append('\'');
		sb.append(", statusCode=").append(statusCode);
		sb.append('}');
		return sb.toString();
	}
}
