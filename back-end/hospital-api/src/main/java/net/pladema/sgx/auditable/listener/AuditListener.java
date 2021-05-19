package net.pladema.sgx.auditable.listener;

import ar.lamansys.sgx.shared.auth.user.SecurityContextUtils;
import net.pladema.sgx.auditable.Auditable;
import net.pladema.sgx.auditable.entity.Audit;

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
			audit.setCreatedBy(getCurrentAuditor());
			audit.setUpdatedBy(getCurrentAuditor());
			auditable.setAudit(audit);
		}
	}

	@PreUpdate
	public void setUpdatedOn(Auditable auditable) {
		auditable.getAudit().ifPresent(a -> {
			a.setUpdatedOn(LocalDateTime.now());
			a.setUpdatedBy(getCurrentAuditor());
		});
	}
	
	public Integer getCurrentAuditor() {
		return SecurityContextUtils.getUserDetails().userId;
	}

}