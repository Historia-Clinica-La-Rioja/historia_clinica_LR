package net.pladema.medicine.infrastructure.output;

import net.pladema.medicine.application.port.MedicineFinancingStatusSearchStorage;
import net.pladema.medicine.domain.MedicineFinancingStatusFilterBo;
import net.pladema.medicine.infrastructure.output.repository.MedicineFinancingStatusRepository;
import lombok.AllArgsConstructor;
import net.pladema.medicine.infrastructure.input.rest.dto.MedicineFinancingStatusDto;
import net.pladema.medicine.domain.MedicineFinancingStatusBo;
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
public class BackofficeMedicineFinancingStatusStore implements BackofficeStore<MedicineFinancingStatusDto, Integer> {

	private final MedicineFinancingStatusRepository repository;
	private final MedicineFinancingStatusSearchStorage medicineFinancingStatusSearchStorage;

	
	@Override
	public Page<MedicineFinancingStatusDto> findAll(MedicineFinancingStatusDto example, Pageable pageable) {
		MedicineFinancingStatusFilterBo filter = new MedicineFinancingStatusFilterBo(example.getConceptSctid(), example.getConceptPt(), example.getFinanced(), null);
		return medicineFinancingStatusSearchStorage.findAllByFilter(filter, pageable).map(this::mapToDto);
	}

	@Override
	public List<MedicineFinancingStatusDto> findAll() {
		return repository.getAll().stream().map(this::mapToDto).collect(Collectors.toList());
	}

	@Override
	public List<MedicineFinancingStatusDto> findAllById(List<Integer> ids) {
		return repository.getAllById(ids).stream().map(this::mapToDto).collect(Collectors.toList());
	}

	@Override
	public Optional<MedicineFinancingStatusDto> findById(Integer id) {
		return repository.findMedicineById(id).map(this::mapToDto);
	}

	@Override
	public MedicineFinancingStatusDto save(MedicineFinancingStatusDto entity) {
		repository.findById(entity.getId()).ifPresent(medicine -> {
			medicine.setFinanced(entity.getFinanced());
			repository.save(medicine);
		});
		return entity;
	}

	@Override
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

}
