package net.pladema.sgx.backoffice.rest;

import net.pladema.sgx.acl.service.AclCrudPermissionValidator;

public class AclCrudPermissionValidatorAdapter<E, I> implements AclCrudPermissionValidator<E, I> {
	@Override
	public void assertGetList(E entity) {

	}

	@Override
	public void assertGetOne(I id) {

	}

	@Override
	public void assertCreate(E entity) {

	}

	@Override
	public void assertUpdate(I id) {

	}

	@Override
	public void assertDelete(I id) {

	}
}
