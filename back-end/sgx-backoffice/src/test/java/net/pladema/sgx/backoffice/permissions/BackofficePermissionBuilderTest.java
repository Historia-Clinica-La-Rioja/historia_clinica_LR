package net.pladema.sgx.backoffice.permissions;

import static net.pladema.sgx.backoffice.permissions.BackofficePermissionBuilder.permitAll;
import static net.pladema.sgx.backoffice.permissions.BackofficePermissionBuilder.permitOnly;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThatCode;

import net.pladema.sgx.backoffice.BOMethod;
import net.pladema.sgx.backoffice.exceptions.PermissionDeniedException;

public class BackofficePermissionBuilderTest {
	private static final String ENTITY = "some data";
	private static final Number ID = 1;

	@Test
	public void permitAll_doesPermitAll() {
		BackofficePermissionValidator<String, Number> validator = permitAll();
		assertThatCode(() -> {
			validator.assertGetList(ENTITY);
			validator.assertGetOne(ID);
			validator.assertCreate(ENTITY);
			validator.assertUpdate(ID, ENTITY);
			validator.assertDelete(ID);
		}).doesNotThrowAnyException();
	}

	@Test
	public void permitOnly_permitOnlyNothing() {
		BackofficePermissionValidator<String, Number> validator = permitOnly();
		assertThatThrownBy(() -> {
			validator.assertGetList(ENTITY);
		}).isInstanceOf(PermissionDeniedException.class);
		assertThatThrownBy(() -> {
			validator.assertGetOne(ID);
		}).isInstanceOf(PermissionDeniedException.class);
		assertThatThrownBy(() -> {
			validator.assertCreate(ENTITY);
		}).isInstanceOf(PermissionDeniedException.class);
		assertThatThrownBy(() -> {
			validator.assertUpdate(ID, ENTITY);
		}).isInstanceOf(PermissionDeniedException.class);
		assertThatThrownBy(() -> {
			validator.assertDelete(ID);
		}).isInstanceOf(PermissionDeniedException.class);
	}

	@Test
	public void permitOnly_permitOnlyGetOne() {
		BackofficePermissionValidator<String, Number> validator = permitOnly(BOMethod.GET_ONE);
		assertThatThrownBy(() -> {
			validator.assertGetList(ENTITY);
		}).isInstanceOf(PermissionDeniedException.class);
		assertThatCode(() -> {
			validator.assertGetOne(ID);
		}).doesNotThrowAnyException();
		assertThatThrownBy(() -> {
			validator.assertCreate(ENTITY);
		}).isInstanceOf(PermissionDeniedException.class);
		assertThatThrownBy(() -> {
			validator.assertUpdate(ID, ENTITY);
		}).isInstanceOf(PermissionDeniedException.class);
		assertThatThrownBy(() -> {
			validator.assertDelete(ID);
		}).isInstanceOf(PermissionDeniedException.class);
	}

}