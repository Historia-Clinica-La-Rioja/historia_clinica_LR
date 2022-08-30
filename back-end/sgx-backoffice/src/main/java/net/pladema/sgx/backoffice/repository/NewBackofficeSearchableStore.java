package net.pladema.sgx.backoffice.repository;

import java.util.function.Function;

import net.pladema.sgx.NewQueryByString;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public abstract class NewBackofficeSearchableStore<E, I> implements NewBackofficeStore<E, I> {
	private final NewQueryByString<E> queryByString;

	protected NewBackofficeSearchableStore(NewQueryByString<E> queryByString) {
		this.queryByString = queryByString;
	}

	public Page<E> findAll(E entity, Pageable pageable, String q) {
		return queryByString.findAll("%" + q.toUpperCase() + "%", pageable);
	}

	protected static final <E> NewQueryByString<E> queryNotAllow() {
		return (q, p) -> Page.empty();
	}

	protected static final <D,E> NewQueryByString<D> queryThenMap(NewQueryByString<E> queryByString, Function<E, D> mapper) {
		return (q, p) -> queryByString.findAll(q, p).map(mapper);
	}




}
