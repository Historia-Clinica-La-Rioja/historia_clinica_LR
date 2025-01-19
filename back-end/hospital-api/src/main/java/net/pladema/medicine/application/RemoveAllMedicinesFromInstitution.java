package net.pladema.medicine.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicine.infrastructure.output.repository.InstitutionMedicineGroupRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class RemoveAllMedicinesFromInstitution {

    private final InstitutionMedicineGroupRepository institutionMedicineGroupRepository;

    public void run(Integer institutionId) {
        log.debug("Input parameters -> institutionId {}", institutionId);
        institutionMedicineGroupRepository.deleteByInstitutionId(institutionId);
        log.debug("Output -> {}", Boolean.TRUE);
    }

}
