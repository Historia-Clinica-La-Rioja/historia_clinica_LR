package net.pladema.medicine.infrastructure.input.rest.constraints.validator;

import net.pladema.medicine.infrastructure.output.repository.MedicineGroupProblemRepository;
import net.pladema.medicine.infrastructure.output.repository.MedicineGroupRepository;

import lombok.AllArgsConstructor;
import net.pladema.medicine.infrastructure.output.repository.entity.MedicineGroup;
import net.pladema.sgx.backoffice.validation.BackofficeEntityValidator;

import net.pladema.sgx.exceptions.BackofficeValidationException;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class BackofficeMedicineGroupEntityValidator implements BackofficeEntityValidator<MedicineGroup, Integer> {

	private final MedicineGroupRepository medicineGroupRepository;
	private final MedicineGroupProblemRepository medicineGroupProblemRepository;

	@Override
	public void assertCreate(MedicineGroup entity) {
		assertValidName(entity);
		List<MedicineGroup> medicineGroupList = medicineGroupRepository.findGroupsByName(entity.getName());
		if (!medicineGroupList.isEmpty()){
			throw new BackofficeValidationException("medicine-group.exists");
		} else {
			entity.setId(null);
		}
	}

	@Override
	public void assertUpdate(Integer id, MedicineGroup entity) {
		assertValidName(entity);
		List<MedicineGroup> medicineGroupList = medicineGroupRepository.findGroupsByName(entity.getName())
				.stream()
				.filter(mg -> !mg.getId().equals(id))
				.collect(Collectors.toList());
		if (!medicineGroupList.isEmpty())
			throw new BackofficeValidationException("medicine-group.exists");
		if (entity.getAllDiagnoses())
			assertDiagnosesListEmpty(id);
	}

	@Override
	public void assertDelete(Integer id) {}

	private void assertValidName(MedicineGroup entity){
		if (entity.getName() == null || entity.getName().isBlank())
			throw new BackofficeValidationException("medicine-group.invalid.name");
	}

	protected void assertDiagnosesListEmpty(Integer id){
		if (!medicineGroupProblemRepository.getByMedicineGroupId(id).isEmpty())
			throw new BackofficeValidationException("medicine-group.has.diagnoses");
	}

}
