package net.pladema.medicine.infrastructure.output;

import lombok.AllArgsConstructor;
import net.pladema.medicine.application.port.MedicineFinancingStatusSearchStorage;
import net.pladema.medicine.domain.InstitutionMedicineFinancingStatusBo;
import net.pladema.medicine.domain.MedicineFinancingStatusBo;
import net.pladema.medicine.domain.MedicineFinancingStatusFilterBo;
import net.pladema.medicine.infrastructure.input.rest.dto.InstitutionMedicineFinancingStatusDto;
import net.pladema.medicine.infrastructure.output.repository.InstitutionMedicineFinancingStatusRepository;
import net.pladema.medicine.infrastructure.output.repository.MedicineFinancingStatusRepository;
import net.pladema.sgx.backoffice.repository.BackofficeStore;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class BackofficeInstitutionMedicineFinancingStatusStore implements BackofficeStore<InstitutionMedicineFinancingStatusDto, Integer> {

	private final InstitutionMedicineFinancingStatusRepository repository;
	private final MedicineFinancingStatusSearchStorage medicineFinancingStatusSearchStorage;
	private final MedicineFinancingStatusRepository medicineFinancingStatusRepository;

	@Override
	public Page<InstitutionMedicineFinancingStatusDto> findAll(InstitutionMedicineFinancingStatusDto example, Pageable pageable) {
		Integer institutionId = example.getInstitutionId();
		/* If medicineId = -1 it means that we are adding a medicine to an institutional medicine group, we have to return all financed medicines */
		if (example.getMedicineId() != null && example.getMedicineId().equals(-1)) {
			return medicineFinancingStatusSearchStorage.findAllFinancedInInstitution(institutionId, example.getConceptPt()).map(this::mapToDto);
		} else {
			MedicineFinancingStatusFilterBo filter = new MedicineFinancingStatusFilterBo(example.getConceptSctid(), example.getConceptPt(), example.getFinancedByDomain(), example.getFinancedByInstitution());
			return medicineFinancingStatusSearchStorage.findAllByFilter(institutionId, filter, pageable).map(this::mapToDto);
		}
	}

	@Override
	public List<InstitutionMedicineFinancingStatusDto> findAll() {
		return List.of();
	}

	@Override
	public List<InstitutionMedicineFinancingStatusDto> findAllById(List<Integer> ids) {
		return repository.findAllById(ids).stream().map(this::mapToDto).collect(Collectors.toList());
	}

	@Override
	public Optional<InstitutionMedicineFinancingStatusDto> findById(Integer id) {
		return repository.findBoById(id).map(this::mapToDto);
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
		result.setMedicineId(bo.getMedicineId());
		result.setFinancedByDomain(bo.getFinancedByDomain());
		result.setConceptPt(bo.getConceptPt());
		result.setConceptSctid(bo.getConceptSctid());
		return result;
	}


}
