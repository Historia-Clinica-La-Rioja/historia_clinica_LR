package net.pladema.staff.service;

import net.pladema.clinichistory.hospitalization.service.domain.ClinicalSpecialtyBo;
import net.pladema.staff.repository.ClinicalSpecialtyRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClinicalSpecialtyServiceImpl implements ClinicalSpecialtyService{

    private final ClinicalSpecialtyRepository clinicalSpecialtyRepository;

    public ClinicalSpecialtyServiceImpl(ClinicalSpecialtyRepository clinicalSpecialtyRepository){
        this.clinicalSpecialtyRepository = clinicalSpecialtyRepository;
    }

    @Override
    public Optional<ClinicalSpecialtyBo> getClinicalSpecialty(Integer clinicalSpecialtyId) {
        return Optional.of(clinicalSpecialtyRepository.findClinicalSpecialtyBoById(clinicalSpecialtyId));
    }
}
