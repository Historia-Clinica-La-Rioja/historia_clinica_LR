package net.pladema.auditable.listener;

import net.pladema.auditable.Auditable;
import net.pladema.auditable.entity.Audit;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

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


	private static boolean isAnonymousUser(Authentication authentication){
		return authentication.getPrincipal().equals("anonymousUser");
	}
	
	public Integer getCurrentAuditor() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated() || isAnonymousUser(authentication) )
			return -1;
		return (Integer) authentication.getPrincipal();
	}

}