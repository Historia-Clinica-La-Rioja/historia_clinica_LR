package net.pladema.staff.controller.mapper;

import net.pladema.establishment.repository.EpisodeDocumentTypeRepository;
import net.pladema.sgx.backoffice.repository.BackofficeStore;

import net.pladema.staff.repository.entity.EpisodeDocumentType;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BackofficeEpisodeDocumentTypeStore implements BackofficeStore<EpisodeDocumentType, Integer> {

	private final EpisodeDocumentTypeRepository repository;

	public BackofficeEpisodeDocumentTypeStore(EpisodeDocumentTypeRepository repository) {
		this.repository = repository;
	}

	@Override
	public Page<EpisodeDocumentType> findAll(EpisodeDocumentType example, Pageable pageable) {
		return null;
	}

	@Override
	public List<EpisodeDocumentType> findAll() {
		return null;
	}

	@Override
	public List<EpisodeDocumentType> findAllById(List<Integer> ids) {
		return null;
	}

	@Override
	public Optional<EpisodeDocumentType> findById(Integer id) {
		return Optional.empty();
	}

	@Override
	public EpisodeDocumentType save(EpisodeDocumentType entity) {
		return repository.save(entity);
	}

	@Override
	public void deleteById(Integer id) {

	}

	@Override
	public Example<EpisodeDocumentType> buildExample(EpisodeDocumentType entity) {
		return null;
	}
}
