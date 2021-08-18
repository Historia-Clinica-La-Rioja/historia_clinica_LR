package ar.lamansys.sgx.shared.scheduling.infrastructure.output.repository.synchronization;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import java.time.LocalDateTime;


@Entity
@Table(name = "sync_error")
public class SyncError {

	@Id
	@Column(name = "id", nullable = false)
	private Long id;
	
	@Column(name = "entity", nullable = false)
	private String entity;
	
	@Column(name = "performed_date", nullable = false)
	private LocalDateTime performedDate;
	
	@Column(name = "log", nullable = false)
	private String log;
	
	@PrePersist
	void createdAt() {
		this.performedDate = LocalDateTime.now();
	}

	@PreUpdate
	void updatedAt() {
		this.performedDate = LocalDateTime.now();
	}
	
	public SyncError() {
	}

	public SyncError(Long id, String entity, String log) {
		super();
		this.id = id;
		this.entity = entity;
		this.log = log;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public LocalDateTime getPerformedDate() {
		return performedDate;
	}

	public void setPerformedDate(LocalDateTime performedDate) {
		this.performedDate = performedDate;
	}

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}
	
	
}
