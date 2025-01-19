package net.pladema.medicine.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.medicine.domain.InstitutionMedicineGroupBo;
import net.pladema.medicine.infrastructure.output.repository.InstitutionMedicineGroupRepository;

import net.pladema.medicine.infrastructure.output.repository.MedicineGroupRepository;
import net.pladema.medicine.infrastructure.output.repository.entity.InstitutionMedicineGroup;
import net.pladema.medicine.infrastructure.output.repository.entity.MedicineGroup;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class AssociateAllMedicineGroupsToInstitution {

	private final InstitutionMedicineGroupRepository institutionMedicineGroupRepository;
	private final MedicineGroupRepository medicineGroupRepository;

	public void run (Integer institutionId){
		log.debug("Input parameters -> institutionId {}", institutionId);

		List<Integer> associatedGroups = institutionMedicineGroupRepository.getByInstitutionId(institutionId).stream().map(InstitutionMedicineGroupBo::getId).collect(Collectors.toList());

		List<Integer> groupIds = medicineGroupRepository.findAll().stream()
				.filter(group -> group.getIsDomain() && !associatedGroups.contains(group.getId()))
				.map(MedicineGroup::getId).collect(Collectors.toList());

		groupIds.forEach(groupId -> institutionMedicineGroupRepository.save(new InstitutionMedicineGroup(institutionId, groupId)));
	}
}
