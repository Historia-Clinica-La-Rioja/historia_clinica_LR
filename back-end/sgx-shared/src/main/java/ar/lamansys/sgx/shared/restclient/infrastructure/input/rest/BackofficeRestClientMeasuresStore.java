package ar.lamansys.sgx.shared.restclient.infrastructure.input.rest;


import ar.lamansys.sgx.shared.restclient.infrastructure.output.repository.RestClientMeasure;
import ar.lamansys.sgx.shared.restclient.infrastructure.output.repository.RestClientMeasureRepository;
import net.pladema.sgx.backoffice.repository.BackofficeStore;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BackofficeRestClientMeasuresStore implements BackofficeStore<RestClientMeasure, Long> {
	private final RestClientMeasureRepository restClientMeasureRepository;

	public BackofficeRestClientMeasuresStore(RestClientMeasureRepository restClientMeasureRepository) {
		this.restClientMeasureRepository = restClientMeasureRepository;
	}


	@Override
	public Page<RestClientMeasure> findAll(RestClientMeasure entity, Pageable pageable) {

		ExampleMatcher customExampleMatcher = ExampleMatcher.matchingAny()
				.withMatcher("host", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
		return restClientMeasureRepository.findAll(Example.of(entity, customExampleMatcher), pageable);
	}

	@Override
	public List<RestClientMeasure> findAll() {
		return restClientMeasureRepository.findAll();
	}

	@Override
	public List<RestClientMeasure> findAllById(List<Long> ids) {
		return restClientMeasureRepository.findAllById(ids);
	}

	@Override
	public Optional<RestClientMeasure> findById(Long id) {
		return restClientMeasureRepository.findById(id);
	}

	@Override
	public RestClientMeasure save(RestClientMeasure dto) {
		return restClientMeasureRepository.save(dto);
	}
	
	@Override
	public void deleteById(Long id) {
		restClientMeasureRepository.deleteById(id);
	}

	@Override
	public Example<RestClientMeasure> buildExample(RestClientMeasure entity) {
		return Example.of(entity);
	}


}
