package net.pladema.sgx;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

public class NewBackofficeQueryAdapter<E> {
	private final ExampleMatcher matcher;

	public NewBackofficeQueryAdapter(ExampleMatcher matcher) {
		this.matcher = matcher;
	}

	public Example<E> buildExample(E entity) {
		return Example.of(entity, matcher);
	}
}
