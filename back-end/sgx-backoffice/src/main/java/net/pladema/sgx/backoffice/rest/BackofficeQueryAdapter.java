package net.pladema.sgx.backoffice.rest;

import org.springframework.data.domain.Example;

public class BackofficeQueryAdapter<E> {
	public Example<E> buildExample(E entity) {
		return Example.of(entity);
	}
}
