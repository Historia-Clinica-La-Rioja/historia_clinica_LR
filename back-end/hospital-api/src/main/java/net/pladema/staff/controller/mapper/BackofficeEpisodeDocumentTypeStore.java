package net.pladema.staff.controller.mapper;

import ar.lamansys.sgx.shared.jsoup.utils.XHTMLUtils;
import net.pladema.establishment.repository.EpisodeDocumentTypeRepository;
import net.pladema.sgx.backoffice.repository.BackofficeStore;

import net.pladema.sgx.exceptions.BackofficeValidationException;
import net.pladema.staff.repository.entity.EpisodeDocumentType;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BackofficeEpisodeDocumentTypeStore implements BackofficeStore<EpisodeDocumentType, Integer> {

	private final EpisodeDocumentTypeRepository repository;

	private final static Integer REGULAR_DOCUMENT = 1;

	private final XHTMLUtils XHTMLUtils;

	public BackofficeEpisodeDocumentTypeStore(EpisodeDocumentTypeRepository repository, XHTMLUtils XHTMLUtils) {
		this.repository = repository;
		this.XHTMLUtils = XHTMLUtils;
	}

	@Override
	public Page<EpisodeDocumentType> findAll(EpisodeDocumentType example, Pageable pageable) {
		return repository.findAll(
				PageRequest.of(
						pageable.getPageNumber(),
						pageable.getPageSize(),
						pageable.getSort()
				)
		);
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
		return repository.findById(id);
	}

	@Override
	public EpisodeDocumentType save(EpisodeDocumentType entity) {
		if (entity.getConsentId() == null)
			entity.setConsentId(REGULAR_DOCUMENT);

		if (entity.getRichTextBody() != null)
			entity.setRichTextBody(XHTMLUtils.toXHTML(entity.getRichTextBody(), true));
		return repository.save(entity);
	}

	@Override
	public void deleteById(Integer id) {
		repository.findById(id).ifPresent(episodeDocumentType -> {
			if (episodeDocumentType.getConsentId().equals(1))
				repository.deleteById(id);
			else
				throw new BackofficeValidationException("El documento es de consentimiento y no se puede eliminar");
		});
	}

	@Override
	public Example<EpisodeDocumentType> buildExample(EpisodeDocumentType entity) {
		return null;
	}
}
