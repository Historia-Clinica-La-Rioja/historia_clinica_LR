package net.pladema.medicine.infrastructure.input.rest.constraints.validator;

import lombok.AllArgsConstructor;
import net.pladema.medicine.domain.InstitutionMedicineGroupBo;
import net.pladema.medicine.infrastructure.input.rest.dto.InstitutionMedicineGroupDto;
import net.pladema.medicine.infrastructure.output.repository.InstitutionMedicineGroupRepository;
import net.pladema.sgx.backoffice.validation.BackofficeEntityValidator;

import net.pladema.sgx.exceptions.BackofficeValidationException;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class BackofficeInstitutionMedicineGroupEntityValidator implements BackofficeEntityValidator<InstitutionMedicineGroupDto, Integer> {

	private final BackofficeMedicineGroupEntityValidator medicineGroupEntityValidator;
	private final InstitutionMedicineGroupRepository institutionMedicineGroupRepository;

	@Override
	public void assertCreate(InstitutionMedicineGroupDto entity) {
		List<InstitutionMedicineGroupBo> institutionGroups = institutionMedicineGroupRepository.getByInstitutionId(entity.getInstitutionId())
				.stream()
				.filter(mg -> mg.getEnabled().equals(Boolean.TRUE) && mg.getName().equals(entity.getName()))
				.collect(Collectors.toList());

		if (!institutionGroups.isEmpty())
			throw new BackofficeValidationException("medicine-group.exists");

	}

	@Override
	public void assertUpdate(Integer id, InstitutionMedicineGroupDto entity) {
		List<InstitutionMedicineGroupBo> institutionGroups = institutionMedicineGroupRepository.getByInstitutionId(entity.getInstitutionId())
				.stream()
				.filter(group -> !group.getId().equals(id) && group.getEnabled().equals(Boolean.TRUE) && group.getName().equals(entity.getName()))
				.collect(Collectors.toList());

		if (!institutionGroups.isEmpty())
			throw new BackofficeValidationException("medicine-group.exists");

		if (entity.getAllDiagnoses().equals(Boolean.TRUE))
			medicineGroupEntityValidator.assertDiagnosesListEmpty(entity.getMedicineGroupId());

	}

	@Override
	public void assertDelete(Integer id) {}

}
