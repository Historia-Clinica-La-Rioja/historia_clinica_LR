package net.pladema.parameterizedform.infrastructure.output;

import ar.lamansys.sgh.shared.infrastructure.input.service.forms.SharedParameterDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.forms.SharedParameterizedFormPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.loinc.application.port.LoincCodeStoragePort;
import ar.lamansys.sgh.shared.domain.forms.enums.EParameterType;
import net.pladema.parameter.infrastructure.output.repository.ParameterRepository;
import net.pladema.parameter.infrastructure.output.repository.ParameterTextOptionRepository;
import net.pladema.parameter.infrastructure.output.repository.ParameterUnitOfMeasureRepository;
import net.pladema.parameter.infrastructure.output.repository.entity.Parameter;
import net.pladema.parameterizedform.infrastructure.output.repository.ParameterizedFormParameterRepository;

import net.pladema.parameterizedform.infrastructure.output.repository.ParameterizedFormRepository;

import net.pladema.parameterizedform.infrastructure.output.repository.entity.ParameterizedForm;

import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class SharedParameterizedFormPortImpl implements SharedParameterizedFormPort {

	private final ParameterizedFormParameterRepository parameterizedFormParameterRepository;
	private final ParameterRepository parameterRepository;
	private final ParameterTextOptionRepository parameterTextOptionRepository;
	private final ParameterUnitOfMeasureRepository parameterUnitOfMeasureRepository;
	private final LoincCodeStoragePort loincCodeStoragePort;
	private final ParameterizedFormRepository parameterizedFormRepository;

	@Override
	public List<SharedParameterDto> getParametersByFormId(Integer parameterizedFormId) {
		return parameterizedFormParameterRepository.findAllByParameterizedFormId(parameterizedFormId).stream()
				.map(pfp -> new SharedParameterDto(pfp.getParameterId(), pfp.getOrderNumber()))
				.map(this::completeParameterData)
				.sorted(Comparator.comparing(SharedParameterDto::getOrderNumber)
				.thenComparing(SharedParameterDto::getId))
				.collect(Collectors.toList());
	}

	@Override
	public Optional<String> getFormNameById(Integer id) {
		return parameterizedFormRepository.findById(id).map(ParameterizedForm::getName);
	}

	private SharedParameterDto completeParameterData(SharedParameterDto dto){
		parameterRepository.findById(dto.getId())
				.ifPresent(parameter -> {
					dto.setLoincId(parameter.getLoincId());
					dto.setDescription(parameter.getDescription());
					dto.setType(EParameterType.map(parameter.getTypeId()));
					dto.setInputCount(parameter.getInputCount());
					dto.setSnomedGroupId(parameter.getSnomedGroupId());
					if (parameter.getLoincId() != null)
						dto.setDescription(loincCodeStoragePort.getDescriptionById(parameter.getLoincId()).orElse(null));
				});
		if (dto.getType().equals(EParameterType.OPTIONS_LIST)){
			List<SharedParameterDto.TextOptionDto> textOptions = parameterTextOptionRepository.getAllByParameterId(dto.getId())
					.stream().map(textOption -> new SharedParameterDto.TextOptionDto(textOption.getId(), textOption.getDescription())).collect(Collectors.toList());
			dto.setTextOptions(textOptions);
		}
		if (dto.getType().equals(EParameterType.NUMERIC)){
			SharedParameterDto.UnitOfMeasureDto parameterUnitOfMeasure = parameterUnitOfMeasureRepository.getByParameterId(dto.getId())
					.map(unitOfMeasure -> new SharedParameterDto.UnitOfMeasureDto(unitOfMeasure.getUnitOfMeasureId(), unitOfMeasure.getUnitOfMeasureDescription(), unitOfMeasure.getUnitOfMeasureCode()))
					.orElse(null);
			dto.setUnitOfMeasure(parameterUnitOfMeasure);
		}
		return dto;
	}

}
