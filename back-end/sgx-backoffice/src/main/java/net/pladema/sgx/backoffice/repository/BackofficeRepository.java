package net.pladema.sgx.backoffice.repository;

import net.pladema.sgx.backoffice.rest.BackofficeQueryAdapter;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public class BackofficeRepository<E, I> implements BackofficeStore<E, I>{
	private final JpaRepository<E, I> repository;
	private final BackofficeQueryAdapter<E> queryAdapter;

	public BackofficeRepository(
			JpaRepository<E,I> repository,
			BackofficeQueryAdapter<E> queryAdapter
	) {
		this.repository = repository;
		this.queryAdapter = queryAdapter;
	}

	public BackofficeRepository(
			JpaRepository<E,I> repository
	) {
		this.repository = repository;
		this.queryAdapter = new BackofficeQueryAdapter<>();
	}

	public Page<E> findAll(E entity, Pageable pageable) {
		Example<E> example = queryAdapter.buildExample(entity);
		return repository.findAll(example, pageable);
	}

	@Override
	public List<E> findAll() {
		return repository.findAll();
	}

	public List<E> findAllById(List<I> ids) {
		return repository.findAllById(ids);
	}

	public Optional<E> findById(I id) {
		return repository.findById(id);
	}

	public E save(E entity) {
		return repository.save(entity);
	}

	public void deleteById(I id) {
		repository.deleteById(id);
	}

	@Override
	public Example<E> buildExample(E entity) {
		return queryAdapter.buildExample(entity);
	}
}
