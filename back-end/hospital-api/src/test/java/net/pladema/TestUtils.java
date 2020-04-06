package net.pladema;

import net.pladema.auditable.entity.Audit;
import net.pladema.auditable.entity.AuditableEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class TestUtils {

	private TestUtils() {
		
	}
	
	public static void assertCreateAuditableEntity(AuditableEntity e) {
		Optional<Audit> optAudit = e.getAudit();
		assertThat(optAudit.isPresent())
			.isTrue();
	
		assertThat(optAudit.get().getCreatedOn())
			.isNotNull();
	
		assertThat(optAudit.get().getUpdatedOn())
			.isNotNull();
	}
	
	public static void assertUpdateAuditableEntity(AuditableEntity e) {
		Optional<Audit> optAudit = e.getAudit();
		
		assertThat(optAudit.isPresent())
			.isTrue();
		
		assertThat(optAudit.get().getUpdatedOn())
			.isNotNull();	
	}
}
