package net.pladema.sgx.auditable.entity;

import net.pladema.sgx.auditable.Auditable;

import javax.persistence.Embedded;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Optional;

@MappedSuperclass
public class AuditableEntity implements Auditable, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2446654484732250647L;
	
	@Embedded
	private Audit audit;

	@Override
	public Optional<Audit> getAudit() {
		return Optional.ofNullable(audit);
	}

	@Override
	public void setAudit(Audit audit) {
		this.audit = audit;
	}

	public LocalDateTime getCreatedOn() {
		return getAudit()
				.map(Audit::getCreatedOn)
				.orElse(null);
	}


	public LocalDateTime getUpdatedOn() {
		return getAudit()
				.map(Audit::getUpdatedOn)
				.orElse(null);
	}
}
