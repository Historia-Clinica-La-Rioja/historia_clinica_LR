package net.pladema.internation.repository.listener;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

public class InternationListener {

	@PrePersist
	public void setCreatedOn(InternationAuditableEntity auditable) {
		auditable.setCreatedOn(LocalDateTime.now());
		auditable.setUpdatedOn(LocalDateTime.now());
		auditable.setCreatedBy(getCurrentAuditor());
		auditable.setUpdatedBy(getCurrentAuditor());
	}

	@PreUpdate
	public void setUpdatedOn(InternationAuditableEntity auditable) {
		auditable.setUpdatedOn(LocalDateTime.now());
		auditable.setUpdatedBy(getCurrentAuditor());
	}


	public Integer getCurrentAuditor() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated() ||
				authentication.getPrincipal().equals("anonymousUser"))
			return -1;
		return (Integer) authentication.getPrincipal();
	}

}