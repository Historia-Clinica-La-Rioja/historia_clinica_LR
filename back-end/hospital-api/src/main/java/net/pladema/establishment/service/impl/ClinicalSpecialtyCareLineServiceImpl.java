package net.pladema.establishment.service.impl;

import net.pladema.establishment.repository.ClinicalSpecialtyCareLineRepository;
import net.pladema.establishment.service.ClinicalSpecialtyCareLineService;

import net.pladema.establishment.service.domain.ClinicalSpecialtyBo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClinicalSpecialtyCareLineServiceImpl implements ClinicalSpecialtyCareLineService {

    private static final Logger LOG = LoggerFactory.getLogger(ClinicalSpecialtyCareLineServiceImpl.class);

    private static final String OUTPUT = "Output -> {}";

    private final ClinicalSpecialtyCareLineRepository clinicalSpecialtyCareLineRepository;

    public ClinicalSpecialtyCareLineServiceImpl(ClinicalSpecialtyCareLineRepository clinicalSpecialtyCareLineRepository) {
        this.clinicalSpecialtyCareLineRepository = clinicalSpecialtyCareLineRepository;
    }

    @Override
    public List<ClinicalSpecialtyBo> getClinicalSpecialties(Integer careLineId) {
        LOG.debug("Input parameters -> careLineId {}", careLineId);
        List <ClinicalSpecialtyBo> clinicalSpecialties = clinicalSpecialtyCareLineRepository.getAllByCareLineId(careLineId)
                .stream()
                .map(clinicalSpecialty -> new ClinicalSpecialtyBo(clinicalSpecialty.getId(), clinicalSpecialty.getName()))
                .collect(Collectors.toList());
        LOG.trace(OUTPUT, clinicalSpecialties);
        return clinicalSpecialties;
    }

}
