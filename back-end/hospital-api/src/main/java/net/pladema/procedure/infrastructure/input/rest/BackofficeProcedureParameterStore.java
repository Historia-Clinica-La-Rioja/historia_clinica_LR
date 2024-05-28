package net.pladema.procedure.infrastructure.input.rest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import net.pladema.procedure.domain.ProcedureParameterTypeBo;
import net.pladema.procedure.domain.ProcedureParameterVo;
import net.pladema.procedure.infrastructure.input.rest.dto.ProcedureParameterDto;
import net.pladema.procedure.infrastructure.output.repository.ProcedureParameterRepository;
import net.pladema.procedure.infrastructure.output.repository.ProcedureParameterTextOptionRepository;
import net.pladema.procedure.infrastructure.output.repository.ProcedureParameterUnitOfMeasureRepository;
import net.pladema.procedure.infrastructure.output.repository.entity.ProcedureParameter;
import net.pladema.procedure.infrastructure.output.repository.entity.ProcedureParameterTextOption;
import net.pladema.procedure.infrastructure.output.repository.entity.ProcedureParameterUnitOfMeasure;
import net.pladema.procedure.infrastructure.output.repository.mapper.ProcedureParameterMapper;
import net.pladema.sgx.backoffice.repository.BackofficeStore;

@Service
@AllArgsConstructor
public class BackofficeProcedureParameterStore implements BackofficeStore<ProcedureParameterDto, Integer> {

	private ProcedureParameterRepository procedureParameterRepository;
	private ProcedureParameterUnitOfMeasureRepository procedureParameterUnitOfMeasureRepository;
	private ProcedureParameterTextOptionRepository procedureParameterTextOptionRepository;
	private ProcedureParameterMapper procedureParameterMapper;

	@Override
	public Page<ProcedureParameterDto> findAll(ProcedureParameterDto example, Pageable pageable) {
		ExampleMatcher customExampleMatcher = ExampleMatcher.matchingAny()
				.withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
		List<ProcedureParameterDto> result = procedureParameterRepository
			.findAll(
				Example.of(mapDtoToEntity(example), customExampleMatcher),
				PageRequest.of(0, Integer.MAX_VALUE, pageable.getSort())
			)
				.stream()
				.map(this::mapEntityToDto)
				.peek(dto -> dto.setUnitsOfMeasureIds(procedureParameterUnitOfMeasureRepository.getUnitOfMeasureFromProcedureParameterId(dto.getId())))
				.peek(dto -> dto.setTextOptions(procedureParameterTextOptionRepository.getDescriptionsFromProcedureParameterId(dto.getId())))
				.collect(Collectors.toList());

		int totalElements = result.size();
		int minIndex = pageable.getPageNumber() * pageable.getPageSize();
		int maxIndex = minIndex + pageable.getPageSize();
		return new PageImpl<>(result.subList(minIndex, Math.min(maxIndex, result.size())), pageable, result.isEmpty() ? 0 : totalElements);
	}

	@Override
	public List<ProcedureParameterDto> findAll() {
		return procedureParameterRepository.findAll()
				.stream()
				.map(this::mapEntityToDto)
				.peek(dto -> dto.setUnitsOfMeasureIds(procedureParameterUnitOfMeasureRepository.getUnitOfMeasureFromProcedureParameterId(dto.getId())))
				.peek(dto -> dto.setTextOptions(procedureParameterTextOptionRepository.getDescriptionsFromProcedureParameterId(dto.getId())))
				.collect(Collectors.toList());
	}

	@Override
	public List<ProcedureParameterDto> findAllById(List<Integer> ids) {
		return procedureParameterRepository.findAllById(ids)
				.stream()
				.map(this::mapEntityToDto)
				.peek(dto -> dto.setUnitsOfMeasureIds(procedureParameterUnitOfMeasureRepository.getUnitOfMeasureFromProcedureParameterId(dto.getId())))
				.peek(dto -> dto.setTextOptions(procedureParameterTextOptionRepository.getDescriptionsFromProcedureParameterId(dto.getId())))
				.collect(Collectors.toList());
	}

	@Override
	public Optional<ProcedureParameterDto> findById(Integer id) {
		Optional<ProcedureParameter> optionalProcedureParameterVo = procedureParameterRepository.findById(id);
		if (optionalProcedureParameterVo.isPresent()) {
			ProcedureParameter procedureParameter = optionalProcedureParameterVo.get();
			ProcedureParameterVo procedureParameterVo = new ProcedureParameterVo(procedureParameter.getId(),
					procedureParameter.getProcedureTemplateId(),
					procedureParameter.getLoincId(),
					procedureParameter.getTypeId(),
					procedureParameter.getOrderNumber(),
					procedureParameter.getInputCount(),
					procedureParameter.getSnomedGroupId());
			if (procedureParameterVo.getTypeId().equals(ProcedureParameterTypeBo.NUMERIC))
				procedureParameterVo.setUnitsOfMeasureIds(procedureParameterUnitOfMeasureRepository.getUnitOfMeasureFromProcedureParameterId(procedureParameterVo.getId()));
			else if (procedureParameterVo.getTypeId().equals(ProcedureParameterTypeBo.TEXT_OPTION))
				procedureParameterVo.setTextOptions(procedureParameterTextOptionRepository.getDescriptionsFromProcedureParameterId(procedureParameterVo.getId()));
			return Optional.of(procedureParameterMapper.toProcedureParameterDto(procedureParameterVo));
		}
		return Optional.empty();
	}

	@Override
	public ProcedureParameterDto save(ProcedureParameterDto entity) {
		if (entity.getId() != null) {
			Optional<ProcedureParameter> opProcedureParameter = procedureParameterRepository.findById(entity.getId());
			if (opProcedureParameter.isPresent()) {
				ProcedureParameter procedureParameter = opProcedureParameter.get();

				procedureParameter.setLoincId(entity.getLoincId());

				procedureParameter.setInputCount(entity.getInputCount());

				if (procedureParameter.getTypeId().equals(ProcedureParameterTypeBo.NUMERIC))
					procedureParameterUnitOfMeasureRepository.deleteUnitOfMeasureFromProcedureParameterId(entity.getId());

				if (entity.getTypeId().equals(ProcedureParameterTypeBo.NUMERIC))
					for (Integer unitsOfMeasureId : entity.getUnitsOfMeasureIds())
						procedureParameterUnitOfMeasureRepository.save(new ProcedureParameterUnitOfMeasure(entity.getId(), unitsOfMeasureId));

				if (procedureParameter.getTypeId().equals(ProcedureParameterTypeBo.TEXT_OPTION)) {
					if (entity.getTypeId().equals(ProcedureParameterTypeBo.TEXT_OPTION)) {
						updateTextOptions(entity);
					}
					else {
						procedureParameterTextOptionRepository.deleteTextOptionFromProcedureParameterId(entity.getId());
					}
				} else {
					if (entity.getTypeId().equals(ProcedureParameterTypeBo.TEXT_OPTION)) {
						for (String textOption : entity.getTextOptions()) {
							ProcedureParameterTextOption procedureParameterTextOption = new ProcedureParameterTextOption();
							procedureParameterTextOption.setDescription(textOption);
							procedureParameterTextOption.setProcedureParameterId(entity.getId());
							procedureParameterTextOptionRepository.save(procedureParameterTextOption);
						}
					}
				}
				procedureParameter.setTypeId(entity.getTypeId());
				procedureParameter.setSnomedGroupId(entity.getSnomedGroupId());
				procedureParameterRepository.save(procedureParameter);
			}
			return this.findById(entity.getId()).get();
		} else {
			List<Short> orders = procedureParameterRepository.getLastOrderParameterFromProcedureTemplateId(entity.getProcedureTemplateId());
			if (orders.isEmpty()) entity.setOrderNumber((short) 1);
			else entity.setOrderNumber((short) (orders.get(0) + 1));
			ProcedureParameter entitySaved = procedureParameterRepository.save(mapDtoToEntity(entity));
			if (entity.getTypeId().equals(ProcedureParameterTypeBo.NUMERIC))
				if (entity.getUnitsOfMeasureIds().size() >= entity.getInputCount())
					for (Integer unitsOfMeasureId : entity.getUnitsOfMeasureIds())
						procedureParameterUnitOfMeasureRepository.save(new ProcedureParameterUnitOfMeasure(entitySaved.getId(), unitsOfMeasureId));
			if (entity.getTypeId().equals(ProcedureParameterTypeBo.TEXT_OPTION))
				for (String textOption : entity.getTextOptions()) {
					ProcedureParameterTextOption procedureParameterTextOption = new ProcedureParameterTextOption();
					procedureParameterTextOption.setDescription(textOption);
					procedureParameterTextOption.setProcedureParameterId(entitySaved.getId());
					procedureParameterTextOptionRepository.save(procedureParameterTextOption);
				}
			//ecls
			return this.findById(entitySaved.getId()).get();
		}

	}

	private void updateTextOptions(ProcedureParameterDto entity) {
		List<ProcedureParameterTextOption> optionList = procedureParameterTextOptionRepository.getFromProcedureParameterId(entity.getId());
		List<String> descriptionOptionList = optionList.stream().map(ProcedureParameterTextOption::getDescription).collect(Collectors.toList());
		List<String> newOptionList = entity.getTextOptions();
		for (String s : newOptionList) {
			if (!descriptionOptionList.contains(s)) {
				ProcedureParameterTextOption toAdd = new ProcedureParameterTextOption();
				toAdd.setDescription(s);
				toAdd.setProcedureParameterId(entity.getId());
				procedureParameterTextOptionRepository.save(toAdd);
			}
		}
		for (ProcedureParameterTextOption ppto : optionList) {
			if (!newOptionList.contains(ppto.getDescription()))
				procedureParameterTextOptionRepository.deleteById(ppto.getId());
		}
	}

	@Override
	public void deleteById(Integer id) {
		procedureParameterRepository
		.findById(id)
		.ifPresent((procedureParameter) -> {
			procedureParameterRepository.updateOrderNumberAfterDelete(procedureParameter.getProcedureTemplateId(),procedureParameter.getOrderNumber());

			procedureParameter.delete();
			procedureParameterRepository.save(procedureParameter);

			if (procedureParameter.getTypeId().equals(ProcedureParameterTypeBo.NUMERIC)) {
				procedureParameterUnitOfMeasureRepository.deleteUnitOfMeasureFromProcedureParameterId(id);
			}

			if (procedureParameter.getTypeId().equals(ProcedureParameterTypeBo.TEXT_OPTION)) {
				procedureParameterTextOptionRepository.deleteTextOptionFromProcedureParameterId(id);
			}

			procedureParameterRepository.deleteById(id);
		});

	}

	@Override
	public Example<ProcedureParameterDto> buildExample(ProcedureParameterDto entity) {
		return Example.of(entity);
	}

	private ProcedureParameter mapDtoToEntity(ProcedureParameterDto procedureParameterDto) {
		return new ProcedureParameter(procedureParameterDto.getId(),
				procedureParameterDto.getProcedureTemplateId(),
				procedureParameterDto.getLoincId(),
				procedureParameterDto.getOrderNumber(),
				procedureParameterDto.getTypeId(),
				procedureParameterDto.getInputCount(),
				procedureParameterDto.getSnomedGroupId());
	}

	private ProcedureParameterDto mapEntityToDto(ProcedureParameter procedureParameter) {
		return new ProcedureParameterDto(procedureParameter.getId(),
				procedureParameter.getProcedureTemplateId(),
				procedureParameter.getLoincId(),
				procedureParameter.getTypeId(),
				procedureParameter.getOrderNumber(),
				procedureParameter.getInputCount(),
				procedureParameter.getSnomedGroupId());
	}

	public boolean existsInProcedureTemplateByLoinc(Integer parentProcedureTemplate, Integer loincId) {
		return procedureParameterRepository.findByProcedureTemplateId(parentProcedureTemplate)
		.stream()
		.anyMatch(x -> x.getLoincId().equals(loincId));
	}

	public boolean existsInProcedureTemplateByLoinc(Integer parentProcedureTemplate, Integer loincId, Integer excludeId) {
		return procedureParameterRepository.findByProcedureTemplateId(parentProcedureTemplate)
				.stream()
				.anyMatch(x -> x.getLoincId().equals(loincId) && !x.getId().equals(excludeId));
	}
}
