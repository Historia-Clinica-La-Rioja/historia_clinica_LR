package net.pladema.staff.service;

import net.pladema.establishment.service.domain.CareLineBo;
import net.pladema.establishment.service.impl.CareLineServiceImpl;
import net.pladema.staff.repository.ClinicalSpecialtyRepository;
import net.pladema.staff.repository.entity.ClinicalSpecialty;
import net.pladema.staff.service.domain.ClinicalSpecialtyBo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClinicalSpecialtyServiceImpl implements ClinicalSpecialtyService{

	private static final Logger LOG = LoggerFactory.getLogger(CareLineServiceImpl.class);

	private static final String OUTPUT = "Output -> {}";

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
				.distinct()
                .filter(ClinicalSpecialty::isSpecialty)
                .map(clinicalSpecialty -> new ClinicalSpecialtyBo(clinicalSpecialty.getId(), clinicalSpecialty.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ClinicalSpecialtyBo> getAll(){
        List<ClinicalSpecialtyBo> result = new ArrayList<>();
        clinicalSpecialtyRepository.findAllSpecialties()
                .forEach(clinicalSpecialty -> result.add(mapToBo(clinicalSpecialty)));
        return result;
    }

	@Override
	public List<ClinicalSpecialtyBo> getAllByInstitutionIdAndActiveDiaries(Integer institutionId){
		LOG.debug("Input parameters -> institutionId {}", institutionId);
		List<ClinicalSpecialtyBo> clinicalSpecialties = clinicalSpecialtyRepository.getAllByInstitutionIdAndActiveDiaries(institutionId)
				.stream()
				.map(clinicalSpecialty -> mapToBo(clinicalSpecialty))
				.collect(Collectors.toList());
		LOG.trace(OUTPUT, clinicalSpecialties);
		return clinicalSpecialties;
	}

	@Override
	public List<ClinicalSpecialtyBo> getClinicalSpecialtiesByCareLineIdAndDestinationIntitutionId(Integer careLineId, Integer destinationInstitutionId) {
		LOG.debug("Input parameters -> careLineId, destinationInstitutionId {}", careLineId, destinationInstitutionId);
		List <ClinicalSpecialtyBo> clinicalSpecialties = clinicalSpecialtyRepository.getAllByCareLineIdAndDestinationInstitutionId(careLineId, destinationInstitutionId)
				.stream()
				.map(clinicalSpecialty -> new ClinicalSpecialtyBo(clinicalSpecialty.getId(), clinicalSpecialty.getName()))
				.collect(Collectors.toList());
		LOG.trace(OUTPUT, clinicalSpecialties);
		return clinicalSpecialties;
	}

	private ClinicalSpecialtyBo mapToBo(ClinicalSpecialty entiy){
        return new ClinicalSpecialtyBo(entiy.getId(), entiy.getName());
    }
}
