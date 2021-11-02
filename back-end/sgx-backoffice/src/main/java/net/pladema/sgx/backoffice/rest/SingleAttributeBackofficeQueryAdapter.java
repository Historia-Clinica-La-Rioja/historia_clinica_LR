package net.pladema.sgx.backoffice.rest;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

public class SingleAttributeBackofficeQueryAdapter<E> extends BackofficeQueryAdapter<E> {
	
	private ExampleMatcher matcher;
	
	public SingleAttributeBackofficeQueryAdapter(String attribute) {
		super();
		this.matcher = ExampleMatcher
				.matching()
				.withMatcher(attribute, x -> x.ignoreCase().contains());
	}

	@Override
	public Example<E> buildExample(E entity) {
		return Example.of(entity, this.matcher);
	}

}
