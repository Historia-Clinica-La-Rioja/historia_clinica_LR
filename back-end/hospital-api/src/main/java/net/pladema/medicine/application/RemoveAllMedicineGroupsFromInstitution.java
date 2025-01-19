package net.pladema.medicine.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicine.infrastructure.output.repository.InstitutionMedicineFinancingStatusRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class RemoveAllMedicineGroupsFromInstitution {

    private final InstitutionMedicineFinancingStatusRepository institutionMedicineFinancingStatusRepository;

    public void run(Integer institutionId) {
        log.debug("Input parameters -> institutionId {}", institutionId);
        institutionMedicineFinancingStatusRepository.deleteByInstitutionId(institutionId);
        log.debug("Output -> {}", Boolean.TRUE);
    }

}
