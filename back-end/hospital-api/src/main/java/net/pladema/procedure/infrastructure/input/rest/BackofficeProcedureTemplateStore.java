package net.pladema.procedure.infrastructure.input.rest;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedSnomedPort;
import lombok.AllArgsConstructor;
import net.pladema.procedure.domain.SnomedPracticeVo;
import net.pladema.procedure.infrastructure.input.rest.dto.ProcedureTemplateDto;
import net.pladema.procedure.infrastructure.input.rest.dto.SnomedPracticeDto;
import net.pladema.procedure.infrastructure.output.repository.ProcedureTemplateRepository;
import net.pladema.procedure.infrastructure.output.repository.ProcedureTemplateSnomedRepository;
import net.pladema.procedure.infrastructure.output.repository.entity.ProcedureTemplate;
import net.pladema.procedure.infrastructure.output.repository.entity.ProcedureTemplateSnomed;
import net.pladema.procedure.infrastructure.output.repository.entity.ProcedureTemplateSnomedPK;
import net.pladema.procedure.infrastructure.output.repository.mapper.ProcedureTemplateMapper;
import net.pladema.sgx.backoffice.repository.BackofficeStore;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BackofficeProcedureTemplateStore implements BackofficeStore<ProcedureTemplateDto, Integer> {

	private final ProcedureTemplateRepository procedureTemplateRepository;
	private final ProcedureTemplateSnomedRepository procedureTemplateSnomedRepository;
	private final ProcedureTemplateMapper procedureTemplateMapper;
	private final SharedSnomedPort sharedSnomedPort;

	@Override
	public Page<ProcedureTemplateDto> findAll(ProcedureTemplateDto example, Pageable pageable) {
		ExampleMatcher customExampleMatcher = ExampleMatcher.matchingAny()
				.withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
		List<ProcedureTemplateDto> result = procedureTemplateRepository.findAll(Example.of(mapDtoToEntity(example), customExampleMatcher), PageRequest.of(0, Integer.MAX_VALUE, pageable.getSort()))
				.stream()
				.filter(pt -> !pt.getDeleteable().getDeleted())
				.map(this::mapEntityToDto)
				.peek(dto -> dto.setAssociatedPractices(procedureTemplateSnomedRepository.getAllPracticesByProcedureTemplateId(dto.getId())
						.stream()
						.map(this::mapSnomedPracticeVoToDto)
						.collect(Collectors.toList())))
				.collect(Collectors.toList());
		int totalElements = result.size();
		int minIndex = pageable.getPageNumber() * pageable.getPageSize();
		int maxIndex = minIndex + pageable.getPageSize();
		return new PageImpl<>(result.subList(minIndex, Math.min(maxIndex, result.size())), pageable, result.isEmpty() ? 0 : totalElements);
	}

	@Override
	public List<ProcedureTemplateDto> findAll() {
		return procedureTemplateRepository.getAllActivesProcedureTemplates()
				.stream()
				.map(this::mapEntityToDto)
				.peek(dto -> dto.setAssociatedPractices(procedureTemplateSnomedRepository.getAllPracticesByProcedureTemplateId(dto.getId())
						.stream()
						.map(this::mapSnomedPracticeVoToDto)
						.collect(Collectors.toList())))
				.collect(Collectors.toList());
	}

	@Override
	public List<ProcedureTemplateDto> findAllById(List<Integer> ids) {
		return procedureTemplateRepository.getAllActivesProcedureTemplateByIds(ids)
				.stream()
				.map(this::mapEntityToDto)
				.peek(dto -> dto.setAssociatedPractices(procedureTemplateSnomedRepository.getAllPracticesByProcedureTemplateId(dto.getId())
						.stream()
						.map(this::mapSnomedPracticeVoToDto)
						.collect(Collectors.toList())))
				.collect(Collectors.toList());
	}

	@Override
	public Optional<ProcedureTemplateDto> findById(Integer id) {
		Optional<ProcedureTemplateDto> opResult = procedureTemplateRepository.findActiveProcedureTemplateById(id).map(this::mapEntityToDto);
		if (opResult.isPresent()) {
			ProcedureTemplateDto result = opResult.get();
			result.setAssociatedPractices(procedureTemplateSnomedRepository.getAllPracticesByProcedureTemplateId(result.getId())
					.stream()
					.map(this::mapSnomedPracticeVoToDto)
					.collect(Collectors.toList()));
			return Optional.of(result);
		}
		return Optional.empty();
	}

	@Override
	public ProcedureTemplateDto save(ProcedureTemplateDto entity) {
		if ((entity.getId() != null) && (procedureTemplateRepository.existsById(entity.getId())))
			if (entity.getAssociatedPractices() != null)
				for (SnomedPracticeDto associatedPractice : entity.getAssociatedPractices()) {
					Integer snomedId = sharedSnomedPort.getSnomedIdByTerm(associatedPractice.getSctid(), associatedPractice.getPt());
					if (snomedId != null && !procedureTemplateSnomedRepository.existsById(new ProcedureTemplateSnomedPK(entity.getId(), snomedId)))
						procedureTemplateSnomedRepository.save(new ProcedureTemplateSnomed(entity.getId(), snomedId));
				}
		return entity;
	}

	@Override
	public void deleteById(Integer id) {
	}

	public void deleteByProcedureTemplateIdSctid(Integer id, Integer snomedId) {
		procedureTemplateSnomedRepository.deleteById(new ProcedureTemplateSnomedPK(id,snomedId));
	}

	@Override
	public Example<ProcedureTemplateDto> buildExample(ProcedureTemplateDto entity) {
		return Example.of(entity);
	}

	private ProcedureTemplateDto mapEntityToDto(ProcedureTemplate procedureTemplate) {
		return new ProcedureTemplateDto(procedureTemplate.getId(),
				procedureTemplate.getUuid(),
				procedureTemplate.getDescription(),
				null);
	}

	private ProcedureTemplate mapDtoToEntity(ProcedureTemplateDto procedureTemplateDto) {
		return new ProcedureTemplate(procedureTemplateDto.getId(),
				procedureTemplateDto.getUuid(),
				procedureTemplateDto.getDescription());
	}

	private SnomedPracticeDto mapSnomedPracticeVoToDto(SnomedPracticeVo snomedPracticeVo){
		return new SnomedPracticeDto(snomedPracticeVo.getId(), snomedPracticeVo.getSctid(), snomedPracticeVo.getPt());
	}

}
