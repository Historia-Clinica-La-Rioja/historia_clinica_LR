package net.pladema.parameterizedform.infrastructure.output;

import lombok.RequiredArgsConstructor;
import net.pladema.parameterizedform.domain.enums.EFormStatus;
import net.pladema.parameterizedform.infrastructure.output.repository.ParameterizedFormRepository;
import net.pladema.parameterizedform.infrastructure.output.repository.entity.ParameterizedForm;
import net.pladema.sgx.backoffice.repository.BackofficeStore;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BackofficeParameterizedFormStore implements BackofficeStore<ParameterizedForm, Integer> {

	private final ParameterizedFormRepository parameterizedFormRepository;

	@Override
	public Page<ParameterizedForm> findAll(ParameterizedForm example, Pageable pageable) {
		List<ParameterizedForm> result = parameterizedFormRepository.findAll(buildExample(example), pageable.getSort());

		int minIndex = Math.min(pageable.getPageNumber() * pageable.getPageSize(), result.size());
		int maxIndex = Math.min(minIndex + pageable.getPageSize(), result.size());

		return new PageImpl<>(result.subList(minIndex, maxIndex), pageable, result.size());
	}

	@Override
	public List<ParameterizedForm> findAll() {
		return parameterizedFormRepository.findAll();
	}

	@Override
	public List<ParameterizedForm> findAllById(List<Integer> ids) {
		return parameterizedFormRepository.findAllById(ids);
	}

	@Override
	public Optional<ParameterizedForm> findById(Integer id) {
		return parameterizedFormRepository.findById(id);
	}

	@Override
	public ParameterizedForm save(ParameterizedForm entity) {
		entity.setStatusId(EFormStatus.DRAFT.getId());
		return parameterizedFormRepository.save(entity);
	}

	@Override
	public void deleteById(Integer id) {
		parameterizedFormRepository.deleteById(id);
	}

	@Override
	public Example<ParameterizedForm> buildExample(ParameterizedForm entity) {
		ExampleMatcher customExampleMatcher = ExampleMatcher
				.matching()
				.withMatcher("name", x -> x.ignoreCase().contains());
		return Example.of(entity, customExampleMatcher);
	}
}
