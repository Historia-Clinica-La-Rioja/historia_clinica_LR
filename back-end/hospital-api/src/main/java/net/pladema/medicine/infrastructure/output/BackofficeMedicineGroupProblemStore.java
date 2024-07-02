package net.pladema.medicine.infrastructure.output;

import lombok.AllArgsConstructor;
import net.pladema.medicine.domain.MedicineGroupProblemBo;
import net.pladema.medicine.infrastructure.input.rest.dto.MedicineGroupProblemDto;
import net.pladema.medicine.infrastructure.output.repository.MedicineGroupProblemRepository;
import net.pladema.medicine.infrastructure.output.repository.entity.MedicineGroupProblem;
import net.pladema.sgx.backoffice.repository.BackofficeStore;

import net.pladema.snowstorm.repository.SnomedGroupRepository;
import net.pladema.snowstorm.repository.VSnomedGroupConceptRepository;

import net.pladema.snowstorm.repository.entity.VSnomedGroupConcept;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class BackofficeMedicineGroupProblemStore implements BackofficeStore<MedicineGroupProblemDto, Integer> {

	private final MedicineGroupProblemRepository medicineGroupProblemRepository;
	private final VSnomedGroupConceptRepository vSnomedGroupConceptRepository;
	private final SnomedGroupRepository snomedGroupRepository;

	private static final String GROUP_DESCRIPTION = "DIAGNOSIS";
	private static final String CONCEPT_FIELD = "conceptPt";

	@Override
	public Page<MedicineGroupProblemDto> findAll(MedicineGroupProblemDto example, Pageable pageable) {
		PageResult pageResult = new PageResult();
		/* If MedicineGroupId is not present means that we are adding a problem to a group */
		if (example.getMedicineGroupId() == null) {
			pageResult = searchAllProblems(example, pageable);
		}
		/* If MedicineGroupId is present means that we are looking all problems for that group */
		else {
			pageResult = searchGroupProblems(example.getMedicineGroupId(), pageable);
		}
		return new PageImpl<>(pageResult.list, pageable, pageResult.totalElements);
	}

	private PageResult searchAllProblems(MedicineGroupProblemDto example, Pageable pageable){
		PageResult result = new PageResult();
		List<MedicineGroupProblemDto> list = new ArrayList<>();
		var groupId = snomedGroupRepository.getIdByDescriptionAndInstitutionId(GROUP_DESCRIPTION, -1);
		if (groupId.isPresent()) {
			Example<VSnomedGroupConcept> exampleEntity = buildExampleEntity(example, groupId.get());
			list = vSnomedGroupConceptRepository.findAll(exampleEntity, pageable)
					.stream()
					.map(this::mapToDto)
					.collect(Collectors.toList());
		}
		result.setList(list);
		result.setTotalElements(list.size());
		return result;
	}

	private PageResult searchGroupProblems(Integer medicineGroupId, Pageable pageable){
		PageResult result = new PageResult();
		List<MedicineGroupProblemDto> list = medicineGroupProblemRepository.getByMedicineGroupId(medicineGroupId)
				.stream()
				.map(this::mapToDto)
				.collect(Collectors.toList());
		result.setTotalElements(list.size());
		sortList(list, pageable.getSort());
		int minIndex = pageable.getPageNumber() * pageable.getPageSize();
		int maxIndex = minIndex + pageable.getPageSize();
		result.setList(list.subList(minIndex, Math.min(maxIndex, list.size())));
		return result;
	}

	private void sortList(List<MedicineGroupProblemDto> list, Sort sort){
		if (sort.getOrderFor(CONCEPT_FIELD) != null && sort.getOrderFor(CONCEPT_FIELD).isDescending())
			list.sort(Comparator.comparing(dto -> dto.getConceptPt().toLowerCase(), Comparator.reverseOrder()));
		else
			list.sort(Comparator.comparing(dto -> dto.getConceptPt().toLowerCase()));
	}

	@Override
	public List<MedicineGroupProblemDto> findAll() {
		return Collections.emptyList();
	}

	@Override
	public List<MedicineGroupProblemDto> findAllById(List<Integer> ids) {
		return vSnomedGroupConceptRepository.findAllById(ids)
					.stream()
					.map(this::mapToDto)
					.collect(Collectors.toList());
	}

	@Override
	public Optional<MedicineGroupProblemDto> findById(Integer id) {
		return Optional.empty();
	}

	@Override
	public MedicineGroupProblemDto save(MedicineGroupProblemDto entity) {
		MedicineGroupProblem entityToSave = new MedicineGroupProblem();
		entityToSave.setMedicineGroupId(entity.getMedicineGroupId());
		entityToSave.setProblemId(entity.getProblemId());
		entity.setId(medicineGroupProblemRepository.save(entityToSave).getId());
		return entity;
	}

	@Override
	public void deleteById(Integer id) {
		medicineGroupProblemRepository.deleteById(id);
	}

	@Override
	public Example<MedicineGroupProblemDto> buildExample(MedicineGroupProblemDto entity) {
		return Example.of(entity);
	}

	private MedicineGroupProblemDto mapToDto(VSnomedGroupConcept concept){
		MedicineGroupProblemDto result = new MedicineGroupProblemDto();
		result.setId(concept.getConceptId());
		result.setConceptPt(concept.getConceptPt());
		return result;
	}

	private MedicineGroupProblemDto mapToDto(MedicineGroupProblemBo bo){
		MedicineGroupProblemDto result = new MedicineGroupProblemDto();
		result.setId(bo.getId());
		result.setMedicineGroupId(bo.getMedicineGroupId());
		result.setProblemId(bo.getProblemId());
		result.setConceptPt(bo.getConceptPt());
		return result;
	}

	private Example<VSnomedGroupConcept> buildExampleEntity(MedicineGroupProblemDto entity, Integer groupId) {
		VSnomedGroupConcept exampleEntity = new VSnomedGroupConcept();
		exampleEntity.setConceptPt(entity.getConceptPt());
		exampleEntity.setGroupId(groupId);
		ExampleMatcher matcher = ExampleMatcher.matching().withMatcher(CONCEPT_FIELD, x -> x.ignoreCase().contains());
		return Example.of(exampleEntity, matcher);
	}

	static class PageResult {
		List<MedicineGroupProblemDto> list;
		int totalElements;

		private void setList(List<MedicineGroupProblemDto> list) {
			this.list = list;
		}

		private void setTotalElements(int totalElements){
			this.totalElements = totalElements;
		}
	}

}
