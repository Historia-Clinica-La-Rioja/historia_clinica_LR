package net.pladema.parameterizedform.infrastructure.output;

import lombok.RequiredArgsConstructor;
import net.pladema.establishment.repository.InstitutionalParameterizedFormRepository;
import net.pladema.establishment.repository.entity.InstitutionalParameterizedForm;
import net.pladema.parameterizedform.domain.enums.EFormStatus;
import net.pladema.parameterizedform.infrastructure.input.rest.dto.ParameterizedFormDto;
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
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BackofficeParameterizedFormStore implements BackofficeStore<ParameterizedFormDto, Integer> {

	private final ParameterizedFormRepository parameterizedFormRepository;
	private final InstitutionalParameterizedFormRepository institutionalParameterizedFormRepository;

	@Override
	public Page<ParameterizedFormDto> findAll(ParameterizedFormDto example, Pageable pageable) {
		Short excludeResultWithStatusId = example.getStatusId();
		example.setStatusId(null);
		Example<ParameterizedForm> entityExample = buildEntityExample(mapDtoToEntity(example));
		List<ParameterizedFormDto> allResult = parameterizedFormRepository.findAll(entityExample, pageable.getSort()).stream()
				.map(this::mapEntityToDto)
				.peek(dto -> dto.setInstitutionId(null))
				.peek(dto -> dto.setIsEnabled(false))
				.collect(Collectors.toList());

		if (example.getInstitutionId() != null) {
			setInstitutionalParameterizedFormInfoByInstitutionId(allResult, example.getInstitutionId());
		}

		List<ParameterizedFormDto> result = allResult.stream()
				.filter(dto -> {
					boolean statusFilter = !dto.getStatusId().equals(excludeResultWithStatusId);
					boolean domainFilter = true;
					if (example.getIsDomain().equals(false)) {
							domainFilter = example.getInstitutionId().equals(dto.getInstitutionId());
					}

					return statusFilter && domainFilter;
				})
				.collect(Collectors.toList());

		int minIndex = Math.min(pageable.getPageNumber() * pageable.getPageSize(), result.size());
		int maxIndex = Math.min(minIndex + pageable.getPageSize(), result.size());

		return new PageImpl<>(result.subList(minIndex, maxIndex), pageable, result.size());
	}

	@Override
	public List<ParameterizedFormDto> findAll() {
		return  parameterizedFormRepository.findAll().stream()
				.map(this::mapEntityToDto)
				.collect(Collectors.toList());
	}

	@Override
	public List<ParameterizedFormDto> findAllById(List<Integer> ids) {
		return parameterizedFormRepository.findAllById(ids).stream()
				.map(this::mapEntityToDto)
				.collect(Collectors.toList());
	}

	@Override
	public Optional<ParameterizedFormDto> findById(Integer id) {
		Optional<ParameterizedFormDto> parameterizedFormDto = parameterizedFormRepository.findById(id).map(this::mapEntityToDto);

		if (parameterizedFormDto.isPresent()) {
			var result = parameterizedFormDto.get();
			return Optional.of(result);
		}
		return Optional.empty();
	}

	@Override
	public ParameterizedFormDto save(ParameterizedFormDto entity) {
		return (entity.getId() != null) ? updateParameterizedForm(entity) : saveParameterizedForm(entity);
	}

	@Override
	public void deleteById(Integer id) {
		parameterizedFormRepository.deleteById(id);
	}

	@Override
	public Example<ParameterizedFormDto> buildExample(ParameterizedFormDto entity) {
		ExampleMatcher customExampleMatcher = ExampleMatcher
				.matching()
				.withMatcher("name", x -> x.ignoreCase().contains())
				.withMatcher("isDomain", ExampleMatcher.GenericPropertyMatcher::exact);
		return Example.of(entity, customExampleMatcher);
	}

	private Example<ParameterizedForm> buildEntityExample(ParameterizedForm entity) {
		ExampleMatcher customExampleMatcher = ExampleMatcher
				.matching()
				.withMatcher("name", x -> x.ignoreCase().contains())
				.withMatcher("isDomain", ExampleMatcher.GenericPropertyMatcher::exact);
		return Example.of(entity, customExampleMatcher);
	}

	private ParameterizedFormDto saveParameterizedForm(ParameterizedFormDto entity) {
		entity.setStatusId(EFormStatus.DRAFT.getId());
		entity.setIsDomain(entity.getInstitutionId() == null);
		ParameterizedForm saved = parameterizedFormRepository.save(mapDtoToEntity(entity));
		if (entity.getInstitutionId() == null)
			return mapEntityToDto(saved);

		InstitutionalParameterizedForm institutionalParameterizedFormSaved = saveInstitutionalParameterizedForm(saved.getId(), entity);
		return mapEntitiesToDto(saved, institutionalParameterizedFormSaved);
	}

	private InstitutionalParameterizedForm saveInstitutionalParameterizedForm(Integer parameterizedFormId, ParameterizedFormDto entity) {
		entity.setIsEnabled(true);
		InstitutionalParameterizedForm institutionalParameterizedForm = new InstitutionalParameterizedForm(
				parameterizedFormId,
				entity.getInstitutionId(),
				entity.getIsEnabled()
		);

		return institutionalParameterizedFormRepository.save(institutionalParameterizedForm);
	}

	private ParameterizedFormDto updateParameterizedForm(ParameterizedFormDto entity) {
		ParameterizedForm oldEntity = parameterizedFormRepository.getById(entity.getId());
		setNewInformation(oldEntity, entity);
		parameterizedFormRepository.save(oldEntity);
		if (entity.getInstitutionId() == null)
			return mapEntityToDto(oldEntity);

		InstitutionalParameterizedForm oldInstitutionalForm = institutionalParameterizedFormRepository.getByParameterizedFormIdAndInstitutionId(oldEntity.getId(), entity.getInstitutionId()).get();
		oldInstitutionalForm.setIsEnabled(true);
		institutionalParameterizedFormRepository.save(oldInstitutionalForm);
		return mapEntitiesToDto(oldEntity, oldInstitutionalForm);
	}

	private void setNewInformation(ParameterizedForm oldEntity, ParameterizedFormDto newEntity) {
		oldEntity.setName(newEntity.getName());
		oldEntity.setStatusId(EFormStatus.DRAFT.getId());
		oldEntity.setIsDomain(oldEntity.getIsDomain());
		oldEntity.setOutpatientEnabled(newEntity.getOutpatientEnabled());
		oldEntity.setEmergencyCareEnabled(newEntity.getEmergencyCareEnabled());
		oldEntity.setInternmentEnabled(newEntity.getInternmentEnabled());
	}

	private ParameterizedForm mapDtoToEntity(ParameterizedFormDto dto) {
		return new ParameterizedForm (
				dto.getId(),
				dto.getName(),
				dto.getStatusId(),
				dto.getOutpatientEnabled(),
				dto.getInternmentEnabled(),
				dto.getEmergencyCareEnabled(),
				dto.getIsDomain()
		);
	}

	private ParameterizedFormDto mapEntityToDto(ParameterizedForm entity) {
		return new ParameterizedFormDto (
				entity.getId(),
				entity.getName(),
				entity.getStatusId(),
				entity.getOutpatientEnabled(),
				entity.getInternmentEnabled(),
				entity.getEmergencyCareEnabled(),
				entity.getIsDomain()
		);
	}

	private ParameterizedFormDto mapEntitiesToDto(ParameterizedForm pf, InstitutionalParameterizedForm ipf) {
		return new ParameterizedFormDto (
				pf.getId(),
				pf.getName(),
				pf.getStatusId(),
				pf.getOutpatientEnabled(),
				pf.getInternmentEnabled(),
				pf.getEmergencyCareEnabled(),
				pf.getIsDomain(),
				ipf.getInstitutionId(),
				ipf.getIsEnabled()
		);
	}

	private void setInstitutionalParameterizedFormInfoByInstitutionId(List<ParameterizedFormDto> result, Integer institutionId) {
		result.forEach(dto -> {
			List<InstitutionalParameterizedForm> institutionalParameterizedForms = institutionalParameterizedFormRepository.getByParameterizedFormId(dto.getId());
			if (!institutionalParameterizedForms.isEmpty()) {
				institutionalParameterizedForms.stream()
						.filter(ipf -> ipf.getInstitutionId().equals(institutionId))
						.findFirst()
						.ifPresent(ipf -> {
							dto.setInstitutionId(ipf.getInstitutionId());
							dto.setIsEnabled(ipf.getIsEnabled());
						});
			}
		});
	}

}
