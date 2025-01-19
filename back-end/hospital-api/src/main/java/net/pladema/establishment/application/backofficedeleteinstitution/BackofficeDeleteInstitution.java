package net.pladema.establishment.application.backofficedeleteinstitution;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.establishment.repository.InstitutionRepository;
import net.pladema.medicine.application.RemoveAllMedicineGroupsFromInstitution;
import net.pladema.medicine.application.RemoveAllMedicinesFromInstitution;
import net.pladema.permissions.service.InstitutionRoleAssignmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BackofficeDeleteInstitution {

    private final InstitutionRoleAssignmentService institutionRoleAssignmentService;
    private final InstitutionRepository institutionRepository;
    private final RemoveAllMedicinesFromInstitution removeAllMedicinesFromInstitution;
    private final RemoveAllMedicineGroupsFromInstitution removeAllMedicineGroupsFromInstitution;

    @Transactional
    public void run(Integer id) {
        log.debug("Input parameters -> institutionId {}", id);
        removeAllMedicinesFromInstitution.run(id);
        removeAllMedicineGroupsFromInstitution.run(id);
        institutionRoleAssignmentService.removeAllPermissionsFromInstitution(id);
        institutionRepository.deleteById(id);
        institutionRepository.flush();
        log.debug("Output -> {}",Boolean.TRUE);
    }
}
