package net.pladema.sgh.app;

import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class TestUtils {

	private static SGXAuditableEntity e;

	private TestUtils() {

	}

	public static void assertCreateAuditableEntity(SGXAuditableEntity e) {

		assertThat(e.getCreatedOn())
				.isNotNull();

		assertThat(e.getUpdatedOn())
				.isNotNull();
	}

	public static void assertUpdateAuditableEntity(SGXAuditableEntity e) {

		assertThat(e.getUpdatedOn())
				.isNotNull();
	}

	public static void assertDeleteAuditableEntity(SGXAuditableEntity e) {

		assertThat(e.isDeleted()).isTrue();
	}
}
