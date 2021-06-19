package net.pladema.staff.service;

import net.pladema.staff.repository.ClinicalSpecialtyRepository;
import net.pladema.staff.service.domain.ClinicalSpecialtyBo;
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
