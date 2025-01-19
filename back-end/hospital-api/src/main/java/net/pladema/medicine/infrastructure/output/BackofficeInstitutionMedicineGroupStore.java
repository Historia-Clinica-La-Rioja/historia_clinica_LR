package net.pladema.medicine.infrastructure.output;

import lombok.AllArgsConstructor;
import net.pladema.establishment.repository.InstitutionRepository;
import net.pladema.medicine.domain.InstitutionMedicineGroupBo;
import net.pladema.medicine.infrastructure.input.rest.dto.InstitutionMedicineGroupDto;
import net.pladema.medicine.infrastructure.output.repository.InstitutionMedicineGroupRepository;
import net.pladema.medicine.infrastructure.output.repository.MedicineGroupRepository;
import net.pladema.medicine.infrastructure.output.repository.entity.InstitutionMedicineGroup;
import net.pladema.medicine.infrastructure.output.repository.entity.MedicineGroup;
import net.pladema.sgx.backoffice.repository.BackofficeStore;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class BackofficeInstitutionMedicineGroupStore implements BackofficeStore<InstitutionMedicineGroupDto, Integer> {

	private final static String NAME_FIELD = "name";

	private final InstitutionMedicineGroupRepository repository;
	private final InstitutionRepository institutionRepository;
	private final MedicineGroupRepository medicineGroupRepository;

	@Override
	public Page<InstitutionMedicineGroupDto> findAll(InstitutionMedicineGroupDto example, Pageable pageable) {
		List<InstitutionMedicineGroupDto> list = repository.getByInstitutionId(example.getInstitutionId())
				.stream()
				.filter(entity -> entity.getInstitutionId().equals(example.getInstitutionId()) && (example.getIsDomain() == null || example.getIsDomain().equals(entity.getIsDomain())))
				.map(this::mapToDto)
				.collect(Collectors.toList());

		sortList(list, pageable.getSort());

		int minIndex = pageable.getPageNumber()*pageable.getPageSize();
		int maxIndex = Math.min(minIndex + pageable.getPageSize(), list.size());

		return new PageImpl<>(list.subList(minIndex, maxIndex), pageable, list.size());
	}

	@Override
	public List<InstitutionMedicineGroupDto> findAll() {
		return repository.getAll().stream().map(this::mapToDto).collect(Collectors.toList());
	}

	@Override
	public List<InstitutionMedicineGroupDto> findAllById(List<Integer> ids) {
		return repository.getAllByIds(ids).stream()
				.map(this::mapToDto)
				.map(this::completeMedicineGroupData)
				.collect(Collectors.toList());
	}

	@Override
	public Optional<InstitutionMedicineGroupDto> findById(Integer id) {
		return repository.getGroupById(id).map(this::mapToDto).map(this::completeMedicineGroupData);
	}

	@Override
	public InstitutionMedicineGroupDto save(InstitutionMedicineGroupDto entity) {
		if (entity.getId() == null) {
			createGroup(entity);
		} else {
			updateGroup(entity);
		}
		return entity;
	}

	@Override
	public void deleteById(Integer id) {
		repository.deleteById(id);
		Integer groupId = repository.findById(id).map(InstitutionMedicineGroup::getMedicineGroupId).orElse(null);
		if (groupId != null) medicineGroupRepository.deleteById(groupId);
	}

	@Override
	public Example<InstitutionMedicineGroupDto> buildExample(InstitutionMedicineGroupDto entity) {
		return Example.of(entity);
	}

	public void associateGroupToAllInstitutions(Integer groupId){
		if (groupId == null)
			return;
		List<Integer> institutionsIds = institutionRepository.getAllIds();
		institutionsIds.forEach(id -> {
			InstitutionMedicineGroup entity = new InstitutionMedicineGroup(id, groupId);
			repository.save(entity);
		});
	}

	private InstitutionMedicineGroupDto mapToDto(InstitutionMedicineGroupBo bo){
		InstitutionMedicineGroupDto result = new InstitutionMedicineGroupDto();
		result.setId(bo.getId());
		result.setInstitutionId(bo.getInstitutionId());
		result.setMedicineGroupId(bo.getMedicineGroupId());
		result.setName(bo.getName());
		result.setEnabled(bo.getEnabled());
		result.setIsDomain(bo.getIsDomain());
		return result;
	}

	private InstitutionMedicineGroupDto completeMedicineGroupData (InstitutionMedicineGroupDto dto){
		medicineGroupRepository.findById(dto.getMedicineGroupId())
				.ifPresent(group ->{
					dto.setRequiresAudit(group.getRequiresAudit());
					dto.setOutpatient(group.getOutpatient());
					dto.setEmergencyCare(group.getEmergencyCare());
					dto.setInternment(group.getInternment());
					dto.setAllDiagnoses(group.getAllDiagnoses());
					dto.setMessage(group.getMessage());
					dto.setRequiredDocumentation(group.getRequiredDocumentation());
		});
		return dto;
	}

	private void createGroup(InstitutionMedicineGroupDto dto){
		MedicineGroup groupEntity = MedicineGroup.builder()
				.name(dto.getName())
				.requiresAudit(dto.getRequiresAudit())
				.outpatient(dto.getOutpatient())
				.emergencyCare(dto.getEmergencyCare())
				.internment(dto.getInternment())
				.allDiagnoses(dto.getAllDiagnoses())
				.message(dto.getMessage())
				.isDomain(Boolean.FALSE)
				.requiredDocumentation(dto.getRequiresAudit().equals(Boolean.TRUE) ? dto.getRequiredDocumentation() : null)
				.build();

		Integer groupId = medicineGroupRepository.save(groupEntity).getId();

		InstitutionMedicineGroup entity = new InstitutionMedicineGroup(dto.getInstitutionId(), groupId);

		Integer id = repository.save(entity).getId();
		dto.setId(id);
	}

	private void updateGroup(InstitutionMedicineGroupDto dto){
		Boolean isDomain = medicineGroupRepository.findById(dto.getMedicineGroupId()).map(MedicineGroup::getIsDomain).orElse(null);
		if (isDomain != null && isDomain.equals(Boolean.TRUE))
			updateDomainGroup(dto);
		else
			updateInstitutionGroup(dto);
	}

	private void updateInstitutionGroup(InstitutionMedicineGroupDto dto){
		medicineGroupRepository.findById(dto.getMedicineGroupId())
				.ifPresent(medicineGroup -> {
					medicineGroup.setName(dto.getName());
					medicineGroup.setRequiresAudit(dto.getRequiresAudit());
					medicineGroup.setOutpatient(dto.getOutpatient());
					medicineGroup.setInternment(dto.getInternment());
					medicineGroup.setEmergencyCare(dto.getEmergencyCare());
					medicineGroup.setMessage(dto.getMessage());
					medicineGroup.setAllDiagnoses(dto.getAllDiagnoses());
					medicineGroup.setRequiredDocumentation(dto.getRequiresAudit().equals(Boolean.TRUE) ? dto.getRequiredDocumentation() : null);
					medicineGroupRepository.save(medicineGroup);
				});
	}

	private void updateDomainGroup(InstitutionMedicineGroupDto dto){
		repository.findById(dto.getId())
				.ifPresent(group -> {
					if (dto.getEnabled().equals(Boolean.TRUE))
						repository.setDeletedFalse(dto.getId());
					else
						repository.deleteById(dto.getId());
				});
	}

	private void sortList(List<InstitutionMedicineGroupDto> list, Sort sort){
		if (sort.getOrderFor(NAME_FIELD) != null && sort.getOrderFor(NAME_FIELD).isDescending())
			list.sort(Comparator.comparing(dto -> normalizeString(dto.getName()), Comparator.reverseOrder()));
		else
			list.sort(Comparator.comparing(dto -> normalizeString(dto.getName())));
	}

	private static String normalizeString(String string) {
		return StringUtils.stripAccents(string).toLowerCase();
	}

}
