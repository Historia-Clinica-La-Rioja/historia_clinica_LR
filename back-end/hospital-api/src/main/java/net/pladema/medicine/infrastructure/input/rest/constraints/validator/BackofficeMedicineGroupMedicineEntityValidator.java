package net.pladema.medicine.infrastructure.input.rest.constraints.validator;

import lombok.AllArgsConstructor;

import net.pladema.medicine.infrastructure.input.rest.dto.MedicineGroupMedicineDto;
import net.pladema.medicine.infrastructure.output.repository.MedicineGroupMedicineRepository;
import net.pladema.sgx.backoffice.validation.BackofficeEntityValidator;

import net.pladema.sgx.exceptions.BackofficeValidationException;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR', 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
@Component
public class BackofficeMedicineGroupMedicineEntityValidator implements BackofficeEntityValidator<MedicineGroupMedicineDto, Integer> {

	private final MedicineGroupMedicineRepository medicineGroupMedicineRepository;

	@Override
	public void assertCreate(MedicineGroupMedicineDto entity) {
		assertValidFields(entity);
		if (medicineGroupMedicineRepository.getByGroupIdAndMedicineId(entity.getMedicineGroupId(), entity.getMedicineId()).isPresent())
			throw new BackofficeValidationException("medicine-group-medicine.exists.already");
	}

	@Override
	public void assertUpdate(Integer id, MedicineGroupMedicineDto entity) {
		assertValidFields(entity);
	}

	@Override
	public void assertDelete(Integer id) {}

	private void assertValidFields(MedicineGroupMedicineDto entity){
		if (entity.getMedicineGroupId() == null || entity.getMedicineId() == null)
			throw new BackofficeValidationException("medicine-group-medicine.missing.fields");
	}

}
