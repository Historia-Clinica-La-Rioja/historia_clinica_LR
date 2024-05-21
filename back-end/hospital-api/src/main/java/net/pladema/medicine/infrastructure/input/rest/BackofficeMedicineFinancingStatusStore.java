package net.pladema.medicine.infrastructure.input.rest;

import net.pladema.medicine.infrastructure.output.repository.MedicineFinancingStatusRepository;
import lombok.AllArgsConstructor;
import net.pladema.medicine.infrastructure.input.rest.dto.MedicineFinancingStatusDto;
import net.pladema.medicine.domain.MedicineFinancingStatusBo;
import net.pladema.sgx.backoffice.repository.BackofficeStore;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class BackofficeMedicineFinancingStatusStore implements BackofficeStore<MedicineFinancingStatusDto, Integer> {

	private final MedicineFinancingStatusRepository repository;

	
	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
	public Page<MedicineFinancingStatusDto> findAll(MedicineFinancingStatusDto example, Pageable pageable) {
		List<MedicineFinancingStatusDto> result = repository.getAll().stream().map(this::mapToDto).collect(Collectors.toList());
		result = filterResult(example, result);
		sortResult(pageable, result);
		int minIndex = pageable.getPageNumber()*pageable.getPageSize();
		int maxIndex = minIndex + pageable.getPageSize();
		return new PageImpl<>(result.subList(minIndex, Math.min(maxIndex, result.size())), pageable, result.size());
	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
	public List<MedicineFinancingStatusDto> findAll() {
		return repository.getAll().stream().map(this::mapToDto).collect(Collectors.toList());
	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
	public List<MedicineFinancingStatusDto> findAllById(List<Integer> ids) {
		return repository.getAllById(ids).stream().map(this::mapToDto).collect(Collectors.toList());
	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
	public Optional<MedicineFinancingStatusDto> findById(Integer id) {
		return repository.findMedicineById(id).map(this::mapToDto);
	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
	public MedicineFinancingStatusDto save(MedicineFinancingStatusDto entity) {
		repository.findById(entity.getId()).ifPresent(medicine -> {
			medicine.setFinanced(entity.getFinanced());
			repository.save(medicine);
		});
		return entity;
	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
	public void deleteById(Integer id) {}

	@Override
	public Example<MedicineFinancingStatusDto> buildExample(MedicineFinancingStatusDto entity) {
		return Example.of(entity);
	}

	private MedicineFinancingStatusDto mapToDto(MedicineFinancingStatusBo medicineBo){
		MedicineFinancingStatusDto result = new MedicineFinancingStatusDto();
		result.setId(medicineBo.getId());
		result.setConceptPt(medicineBo.getConceptPt());
		result.setConceptSctid(medicineBo.getConceptSctid());
		result.setFinanced(medicineBo.getFinanced());
		return result;
	}

	private List<MedicineFinancingStatusDto> filterResult(MedicineFinancingStatusDto example, List<MedicineFinancingStatusDto> medicines){
		if (example.getConceptPt() != null)
			medicines = medicines.stream().filter(medicine -> medicine.getConceptPt().toLowerCase().contains(example.getConceptPt().toLowerCase())).collect(Collectors.toList());
		if (example.getConceptSctid() != null)
			medicines = medicines.stream().filter(medicine -> medicine.getConceptSctid().contains(example.getConceptSctid())).collect(Collectors.toList());
		if (example.getFinanced() != null)
			medicines = medicines.stream().filter(medicine -> medicine.getFinanced().equals(example.getFinanced())).collect(Collectors.toList());
		return medicines;
	}

	private void sortResult(Pageable pageable, List<MedicineFinancingStatusDto> medicines){
		if (pageable.getSort().getOrderFor("conceptPt") != null){
			if (pageable.getSort().getOrderFor("conceptPt").isDescending())
				medicines.sort(Comparator.comparing(medicine -> medicine.getConceptPt().toLowerCase(), Comparator.reverseOrder()));
			else
				medicines.sort(Comparator.comparing(medicine -> medicine.getConceptPt().toLowerCase()));
		}
		if (pageable.getSort().getOrderFor("conceptSctid") != null){
			if (pageable.getSort().getOrderFor("conceptSctid").isDescending())
				medicines.sort(Comparator.comparing(MedicineFinancingStatusDto::getConceptSctid, Comparator.reverseOrder()));
			else
				medicines.sort(Comparator.comparing(MedicineFinancingStatusDto::getConceptSctid));
		}
	}

}
