package net.pladema.medicine.infrastructure.output;

import lombok.AllArgsConstructor;
import net.pladema.medicine.domain.MedicineGroupMedicineBo;
import net.pladema.medicine.infrastructure.input.rest.dto.MedicineGroupMedicineDto;
import net.pladema.medicine.infrastructure.output.repository.MedicineGroupMedicineRepository;
import net.pladema.medicine.infrastructure.output.repository.entity.MedicineGroupMedicine;
import net.pladema.sgx.backoffice.repository.BackofficeStore;

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
public class BackofficeMedicineGroupMedicineStore implements BackofficeStore<MedicineGroupMedicineDto, Integer> {

	private final MedicineGroupMedicineRepository medicineGroupMedicineRepository;

	private static final String CONCEPT_FIELD = "conceptPt";
	private static final String FINANCED_FIELD = "financed";

	@Override
	public Page<MedicineGroupMedicineDto> findAll(MedicineGroupMedicineDto example, Pageable pageable) {
		List<MedicineGroupMedicineDto> result =
				medicineGroupMedicineRepository.getByMedicineGroupId(example.getMedicineGroupId())
				.stream()
				.map(this::mapToDto)
				.collect(Collectors.toList());
		sortResult(result, pageable.getSort());
		int minIndex = pageable.getPageNumber()*pageable.getPageSize();
		int maxIndex = minIndex + pageable.getPageSize();
		return new PageImpl<>(result.subList(minIndex, Math.min(maxIndex, result.size())), pageable, result.size());
	}

	@Override
	public List<MedicineGroupMedicineDto> findAll() {
		return List.of();
	}

	@Override
	public List<MedicineGroupMedicineDto> findAllById(List<Integer> ids) {
		return List.of();
	}

	@Override
	public Optional<MedicineGroupMedicineDto> findById(Integer id) {
		return Optional.empty();
	}

	@Override
	public MedicineGroupMedicineDto save(MedicineGroupMedicineDto entity) {
		MedicineGroupMedicine entityToSave = new MedicineGroupMedicine();
		entityToSave.setMedicineGroupId(entity.getMedicineGroupId());
		entityToSave.setMedicineId(entity.getMedicineId());
		entity.setId(medicineGroupMedicineRepository.save(entityToSave).getId());
		return entity;
	}

	@Override
	public void deleteById(Integer id) {
		medicineGroupMedicineRepository.deleteById(id);
	}

	@Override
	public Example<MedicineGroupMedicineDto> buildExample(MedicineGroupMedicineDto entity) {
		return Example.of(entity);
	}

	private MedicineGroupMedicineDto mapToDto(MedicineGroupMedicineBo bo){
		MedicineGroupMedicineDto result = new MedicineGroupMedicineDto();
		result.setId(bo.getId());
		result.setMedicineGroupId(bo.getMedicineGroupId());
		result.setMedicineId(bo.getMedicineId());
		result.setFinanced(bo.getFinanced());
		result.setConceptPt(bo.getConceptPt());
		return result;
	}

	private void sortResult(List<MedicineGroupMedicineDto> result, Sort sort){
		if (sort.getOrderFor(CONCEPT_FIELD) != null && sort.getOrderFor(CONCEPT_FIELD).isDescending())
				result.sort(Comparator.comparing(medicine -> medicine.getConceptPt().toLowerCase(), Comparator.reverseOrder()));
			else
				result.sort(Comparator.comparing(medicine -> medicine.getConceptPt().toLowerCase()));

		if (sort.getOrderFor(FINANCED_FIELD) != null){
			if (sort.getOrderFor(FINANCED_FIELD).isDescending())
				result.sort(Comparator.comparing(MedicineGroupMedicineDto::getFinanced, Comparator.reverseOrder()));
			else
				result.sort(Comparator.comparing(MedicineGroupMedicineDto::getFinanced));
		}
	}
}
