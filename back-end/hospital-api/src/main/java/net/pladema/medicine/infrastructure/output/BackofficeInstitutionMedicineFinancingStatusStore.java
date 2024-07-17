package net.pladema.medicine.infrastructure.output;

import lombok.AllArgsConstructor;
import net.pladema.medicine.domain.InstitutionMedicineFinancingStatusBo;
import net.pladema.medicine.infrastructure.input.rest.dto.InstitutionMedicineFinancingStatusDto;
import net.pladema.medicine.infrastructure.input.rest.dto.MedicineFinancingStatusDto;
import net.pladema.medicine.infrastructure.output.repository.InstitutionMedicineFinancingStatusRepository;
import net.pladema.sgx.backoffice.repository.BackofficeStore;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class BackofficeInstitutionMedicineFinancingStatusStore implements BackofficeStore<InstitutionMedicineFinancingStatusDto, Integer> {

	private final InstitutionMedicineFinancingStatusRepository repository;

	@Override
	public Page<InstitutionMedicineFinancingStatusDto> findAll(InstitutionMedicineFinancingStatusDto example, Pageable pageable) {
		List<InstitutionMedicineFinancingStatusDto> result = repository.getByInstitutionId(example.getInstitutionId())
				.stream().map(this::mapToDto).collect(Collectors.toList());

		result = filterResult(example, result);
		sortResult(pageable, result);
		int minIndex = pageable.getPageNumber()*pageable.getPageSize();
		int maxIndex = minIndex + pageable.getPageSize();
		return new PageImpl<>(result.subList(minIndex, Math.min(maxIndex, result.size())), pageable, result.size());
	}

	@Override
	public List<InstitutionMedicineFinancingStatusDto> findAll() {
		return repository.getAll().stream().map(this::mapToDto).collect(Collectors.toList());
	}

	@Override
	public List<InstitutionMedicineFinancingStatusDto> findAllById(List<Integer> ids) {
		return repository.getAllByIds(ids).stream().map(this::mapToDto).collect(Collectors.toList());
	}

	@Override
	public Optional<InstitutionMedicineFinancingStatusDto> findById(Integer id) {
		return repository.findMedicineById(id).map(this::mapToDto);
	}

	@Override
	public InstitutionMedicineFinancingStatusDto save(InstitutionMedicineFinancingStatusDto entity) {
		Integer id = entity.getId();
		repository.findById(id).ifPresent(imfs -> {
			imfs.setFinanced(entity.getFinancedByInstitution());
			repository.save(imfs);
		});
		return entity;
	}

	@Override
	public void deleteById(Integer id) {
		repository.deleteById(id);
	}

	@Override
	public Example<InstitutionMedicineFinancingStatusDto> buildExample(InstitutionMedicineFinancingStatusDto entity) {
		return Example.of(entity);
	}

	private InstitutionMedicineFinancingStatusDto mapToDto(InstitutionMedicineFinancingStatusBo bo){
		InstitutionMedicineFinancingStatusDto result = new InstitutionMedicineFinancingStatusDto();
		result.setId(bo.getId());
		result.setInstitutionId(bo.getInstitutionId());
		result.setFinancedByInstitution(bo.getFinancedByInstitution());
		result.setMedicineId(bo.getId());
		result.setFinancedByDomain(bo.getFinancedByDomain());
		result.setConceptPt(bo.getConceptPt());
		result.setConceptSctid(bo.getConceptSctid());
		return result;
	}

	private List<InstitutionMedicineFinancingStatusDto> filterResult(InstitutionMedicineFinancingStatusDto example, List<InstitutionMedicineFinancingStatusDto> medicines){
		if (example.getConceptPt() != null)
			medicines = medicines.stream().filter(medicine -> normalizeString(medicine.getConceptPt()).contains(normalizeString(example.getConceptPt()))).collect(Collectors.toList());
		if (example.getConceptSctid() != null)
			medicines = medicines.stream().filter(medicine -> medicine.getConceptSctid().contains(example.getConceptSctid())).collect(Collectors.toList());
		if (example.getFinancedByDomain() != null)
			medicines = medicines.stream().filter(medicine -> medicine.getFinancedByDomain().equals(example.getFinancedByDomain())).collect(Collectors.toList());
		if (example.getFinancedByInstitution() != null)
			medicines = medicines.stream().filter(medicine -> medicine.getFinancedByInstitution().equals(example.getFinancedByInstitution())).collect(Collectors.toList());
		return medicines;
	}

	private void sortResult(Pageable pageable, List<InstitutionMedicineFinancingStatusDto> medicines){
		if (pageable.getSort().getOrderFor("conceptPt") != null){
			if (pageable.getSort().getOrderFor("conceptPt").isDescending())
				medicines.sort(Comparator.comparing(medicine -> normalizeString(medicine.getConceptPt()), Comparator.reverseOrder()));
			else
				medicines.sort(Comparator.comparing(medicine -> normalizeString(medicine.getConceptPt())));
		}
		if (pageable.getSort().getOrderFor("conceptSctid") != null){
			if (pageable.getSort().getOrderFor("conceptSctid").isDescending())
				medicines.sort(Comparator.comparing(InstitutionMedicineFinancingStatusDto::getConceptSctid, Comparator.reverseOrder()));
			else
				medicines.sort(Comparator.comparing(InstitutionMedicineFinancingStatusDto::getConceptSctid));
		}
	}

	private static String normalizeString(String string){
		return StringUtils.stripAccents(string).toLowerCase();
	}

}
