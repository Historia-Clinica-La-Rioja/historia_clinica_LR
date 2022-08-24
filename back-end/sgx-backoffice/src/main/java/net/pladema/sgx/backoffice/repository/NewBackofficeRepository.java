package net.pladema.sgx.backoffice.repository;

import java.util.List;
import java.util.Optional;

import net.pladema.sgx.NewQueryByString;

import net.pladema.sgx.NewBackofficeQueryAdapter;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public class NewBackofficeRepository<E, I> extends NewBackofficeSearchableStore<E, I> {
    private final JpaRepository<E, I> repository;
    private final NewBackofficeQueryAdapter<E> queryAdapter;

    public NewBackofficeRepository(
            JpaRepository<E, I> repository,
            NewBackofficeQueryAdapter<E> queryAdapter,
            NewQueryByString<E> queryByString
    ) {
        super(queryByString);
        this.repository = repository;
        this.queryAdapter = queryAdapter;
    }

	public Page<E> findAll(E entity, Pageable pageable) {
        Example<E> example = queryAdapter.buildExample(entity);
        return repository.findAll(example, pageable);
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
}
