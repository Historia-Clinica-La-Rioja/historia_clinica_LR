package net.pladema.procedure.infrastructure.input.rest;

import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgh.clinichistory.domain.ips.services.SnomedService;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedSnomedDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedSnomedPort;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import lombok.AllArgsConstructor;
import net.pladema.procedure.domain.SnomedPracticeVo;
import net.pladema.procedure.infrastructure.input.rest.dto.ProcedureTemplateDto;
import net.pladema.procedure.infrastructure.input.rest.dto.SnomedPracticeDto;
import net.pladema.procedure.infrastructure.output.repository.ProcedureTemplateRepository;
import net.pladema.procedure.infrastructure.output.repository.ProcedureTemplateSnomedRepository;
import net.pladema.procedure.infrastructure.output.repository.entity.ProcedureTemplate;
import net.pladema.procedure.infrastructure.output.repository.entity.ProcedureTemplateSnomed;
import net.pladema.procedure.infrastructure.output.repository.entity.ProcedureTemplateSnomedPK;
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
	private final SharedSnomedPort sharedSnomedPort;
	private final SnomedService snomedService;
	private final FeatureFlagsService featureFlagsService;

	@Override
	public Page<ProcedureTemplateDto> findAll(ProcedureTemplateDto example, Pageable pageable) {
		List<ProcedureTemplateDto> result = procedureTemplateRepository
				.findAll(
					getExample(example),
					PageRequest.of(0, Integer.MAX_VALUE, pageable.getSort())
				)
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

	private Example<ProcedureTemplate> getExample(ProcedureTemplateDto example) {
		ExampleMatcher customExampleMatcher = ExampleMatcher.matchingAny()
				.withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
		var exampleEntity = new ProcedureTemplate(
				example.getId(),
				example.getUuid(),
				example.getDescription(),
				example.getStatusId());
		return Example.of(exampleEntity, customExampleMatcher);
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

	/**
	 * This method's associatedPractices field comes from what BackofficeSnomedPracticesStore#findAll outputs. The
	 * findAll method reads the HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS flag to decide if it should use the snomed cache or not.
	 * How to interpret the associatedPractices input thus depends on whether AppFeature.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS is on or not.
	 *
	 * BackofficeSnomedPracticesStore#findAll outputs:
	 * 	HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS=true
	 *     conceptId = snomed table id
	 *     id = snomed_related_group.id
	 *     the real sctid is missing
	 *     ex.: [{"id":898742,"conceptId":290343,"groupId":16,"groupDescription":"PROCEDURE","conceptPt":"ecografía de arteria periférica"}]
	 *
	 * 	HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS=false
	 *     conceptId = the actual sctid
	 *     id = the sctid again
	 *     example : [{"id":419861003,"conceptId":419861003,"groupDescription":"PROCEDURE","conceptPt":"ecografía de arteria periférica"}]
	 *
	 * What this save method does:
	 * If HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS is on: lookup the snomed term by id
	 * Else: lookup the term by sctid and pt
	 *
	 */
	@Override
	public ProcedureTemplateDto save(ProcedureTemplateDto entity) {
		if ((entity.getId() != null) && (procedureTemplateRepository.existsById(entity.getId())))
			if (entity.getAssociatedPractices() != null)
				for (SnomedPracticeDto associatedPractice : entity.getAssociatedPractices()) {
					Integer conceptId = null;
					if (featureFlagsService.isOn(AppFeature.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS)) {
						SharedSnomedDto concept = sharedSnomedPort.getSnomed(associatedPractice.getId());
						conceptId = concept == null ? null : associatedPractice.getId();
					}
					else {
						var concept = new SnomedBo(associatedPractice.getSctid(), associatedPractice.getPt());
						conceptId = snomedService.getSnomedId(concept).orElseGet(() -> snomedService.createSnomedTerm(concept));
					}
					if (conceptId != null && !procedureTemplateSnomedRepository.existsById(new ProcedureTemplateSnomedPK(entity.getId(), conceptId)))
						procedureTemplateSnomedRepository.save(new ProcedureTemplateSnomed(entity.getId(), conceptId));
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
		return ProcedureTemplateDto.withoutPractices(
				procedureTemplate.getId(),
				procedureTemplate.getUuid(),
				procedureTemplate.getDescription(),
				procedureTemplate.getStatusId());
	}

	private SnomedPracticeDto mapSnomedPracticeVoToDto(SnomedPracticeVo snomedPracticeVo){
		return new SnomedPracticeDto(snomedPracticeVo.getId(), snomedPracticeVo.getSctid(), snomedPracticeVo.getPt());
	}

}
