package ar.lamansys.sgx.shared.auditable.listener;

import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auth.user.SecurityContextUtils;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

public class SGXAuditListener {

	@PrePersist
	public void setCreationable(SGXAuditableEntity auditable) {
		auditable.setCreatedOn(LocalDateTime.now());
		auditable.setUpdatedOn(LocalDateTime.now());
		auditable.setCreatedBy(getCurrentAuditor());
		auditable.setUpdatedBy(getCurrentAuditor());
		auditable.setDeleted(false);
	}

	@PreUpdate
	public void setUpdateable(SGXAuditableEntity auditable) {
		auditable.setUpdatedOn(LocalDateTime.now());
		auditable.setUpdatedBy(getCurrentAuditor());
	}


	public Integer getCurrentAuditor() {
		return SecurityContextUtils.getUserDetails().userId;
	}

}