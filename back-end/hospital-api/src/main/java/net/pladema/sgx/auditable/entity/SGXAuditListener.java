package net.pladema.sgx.auditable.entity;

import ar.lamansys.sgx.shared.auth.user.SecurityContextUtils;

import java.time.LocalDateTime;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class SGXAuditListener {

	@PrePersist
	public void setCreatedOn(SGXAuditableEntity auditable) {
		auditable.setCreatedOn(LocalDateTime.now());
		auditable.setUpdatedOn(LocalDateTime.now());
		auditable.setCreatedBy(getCurrentAuditor());
		auditable.setUpdatedBy(getCurrentAuditor());
	}

	@PreUpdate
	public void setUpdatedOn(SGXAuditableEntity auditable) {
		auditable.setUpdatedOn(LocalDateTime.now());
		auditable.setUpdatedBy(getCurrentAuditor());
	}


	public Integer getCurrentAuditor() {
		return SecurityContextUtils.getUserDetails().userId;
	}

}