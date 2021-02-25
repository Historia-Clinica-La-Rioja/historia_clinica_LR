package net.pladema.emergencycare.service.impl;

import net.pladema.clinichistory.hospitalization.repository.domain.DischargeType;
import net.pladema.emergencycare.repository.DischargeTypeRepository;
import net.pladema.emergencycare.service.DischargeTypeMasterDataService;
import net.pladema.emergencycare.service.domain.DischargeTypeBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DischargeTypeMasterDataServiceImpl implements DischargeTypeMasterDataService {

    private static final Logger LOG = LoggerFactory.getLogger(DischargeTypeMasterDataServiceImpl.class);

    private final DischargeTypeRepository dischargeTypeRepository;

    public DischargeTypeMasterDataServiceImpl(DischargeTypeRepository dischargeTypeRepository) {
        this.dischargeTypeRepository = dischargeTypeRepository;
    }

    @Override
    public List<DischargeTypeBo> getAllInternmentDischargeTypes() {
        LOG.debug("No input parameters");
        return mapToDischargeTypeBos(dischargeTypeRepository.getAllInternmentTypes());
    }

    @Override
    public List<DischargeTypeBo> getAllEmergencyCareDischargeTypes() {
        LOG.debug("No input parameters");
        return mapToDischargeTypeBos(dischargeTypeRepository.getAllEmergencyCareTypes());
    }

    private List<DischargeTypeBo> mapToDischargeTypeBos(List<DischargeType> data) {
        List<DischargeTypeBo> result = data
                .stream()
                .map(DischargeTypeBo::new)
                .collect(Collectors.toList());
        LOG.debug("Output -> {}", result);
        return result;

    }
}
