package net.pladema.staff.service;

import net.pladema.staff.repository.ClinicalSpecialtyRepository;
import net.pladema.staff.repository.entity.ClinicalSpecialty;
import net.pladema.staff.service.domain.ClinicalSpecialtyBo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClinicalSpecialtyServiceImpl implements ClinicalSpecialtyService{

    private final ClinicalSpecialtyRepository clinicalSpecialtyRepository;

    public ClinicalSpecialtyServiceImpl(ClinicalSpecialtyRepository clinicalSpecialtyRepository){
        this.clinicalSpecialtyRepository = clinicalSpecialtyRepository;
    }

    @Override
    public Optional<ClinicalSpecialtyBo> getClinicalSpecialty(Integer clinicalSpecialtyId) {
        return Optional.ofNullable(clinicalSpecialtyRepository.findClinicalSpecialtyBoById(clinicalSpecialtyId));
    }

    @Override
    public List<ClinicalSpecialtyBo> getSpecialtiesByProfessional(Integer professionalId) {
        return clinicalSpecialtyRepository.getAllByProfessional(professionalId)
                .stream()
                .filter(ClinicalSpecialty::isSpecialty)
                .map(clinicalSpecialty -> new ClinicalSpecialtyBo(clinicalSpecialty.getId(), clinicalSpecialty.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ClinicalSpecialtyBo> getAll(){
        List<ClinicalSpecialtyBo> result = new ArrayList<>();
        clinicalSpecialtyRepository.findAll()
                .forEach(clinicalSpecialty -> result.add(mapToBo(clinicalSpecialty)));
        return result;
    }

    private ClinicalSpecialtyBo mapToBo(ClinicalSpecialty entiy){
        return new ClinicalSpecialtyBo(entiy.getId(), entiy.getName());
    }
}
