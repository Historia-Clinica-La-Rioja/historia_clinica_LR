package net.pladema.medicine.infrastructure.input.rest.constraints.validator;

import lombok.AllArgsConstructor;

import net.pladema.medicine.infrastructure.input.rest.dto.MedicineGroupProblemDto;
import net.pladema.medicine.infrastructure.output.repository.MedicineGroupProblemRepository;

import net.pladema.sgx.backoffice.validation.BackofficeEntityValidator;

import net.pladema.sgx.exceptions.BackofficeValidationException;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
@Component
public class BackofficeMedicineGroupProblemEntityValidator implements BackofficeEntityValidator<MedicineGroupProblemDto, Integer> {

	private MedicineGroupProblemRepository medicineGroupProblemRepository;

	@Override
	public void assertCreate(MedicineGroupProblemDto entity) {
		assertValidFields(entity);
		if (medicineGroupProblemRepository.getByGroupIdAndProblemId(entity.getMedicineGroupId(), entity.getProblemId()).isPresent())
			throw new BackofficeValidationException("medicine-group-problem.exists.already");
	}

	@Override
	public void assertUpdate(Integer id, MedicineGroupProblemDto entity) {
		assertValidFields(entity);
	}

	@Override
	public void assertDelete(Integer id) {}

	private void assertValidFields(MedicineGroupProblemDto entity){
		if (entity.getMedicineGroupId() == null || entity.getProblemId() == null)
			throw new BackofficeValidationException("medicine-group-problem.missing.fields");
	}

}
