package net.pladema.sgx;

import net.pladema.sgx.backoffice.repository.NewBackofficeRepository;

import net.pladema.sgx.backoffice.repository.NewBackofficeStore;

import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;


public class NewStoreBuilder {
	private NewStoreBuilder() {}

	public static final <E, I> NewBackofficeStore<E, I> fromJpaSearchable(
			JpaRepository<E, I> repository,
			NewQueryByString<E> queryByString
	) {
		return new NewBackofficeRepository<>(
				repository,
				new NewBackofficeQueryAdapter<>(ExampleMatcher.matching()),
				queryByString
		);
	}

	public static final <E, I> NewBackofficeStore<E, I> fromJpa(
			JpaRepository<E, I> repository
	) {
		return new NewBackofficeRepository<>(
				repository,
				new NewBackofficeQueryAdapter<>(ExampleMatcher.matching()),
				(q, p) -> Page.empty()
		);
	}
}
