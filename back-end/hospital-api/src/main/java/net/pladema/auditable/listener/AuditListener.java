package net.pladema.auditable.listener;

import net.pladema.auditable.Auditable;
import net.pladema.auditable.entity.Audit;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

public class AuditListener {

	@PrePersist
	public void setCreatedOn(Auditable auditable) {
		if (!auditable.getAudit().isPresent()) {
			Audit audit = new Audit();
			audit.setCreatedOn(LocalDateTime.now());
			audit.setUpdatedOn(LocalDateTime.now());
			auditable.setAudit(audit);
		}
	}

	@PreUpdate
	public void setUpdatedOn(Auditable auditable) {
		auditable.getAudit().ifPresent(a -> {
			a.setUpdatedOn(LocalDateTime.now());
		});
	}
}