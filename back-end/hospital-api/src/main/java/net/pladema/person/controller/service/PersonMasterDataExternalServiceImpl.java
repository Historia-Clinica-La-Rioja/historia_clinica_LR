package net.pladema.person.controller.service;

import net.pladema.person.controller.dto.EthnicityDto;
import net.pladema.person.controller.mapper.EthnicityMapper;
import net.pladema.person.service.PersonMasterDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonMasterDataExternalServiceImpl implements PersonMasterDataExternalService {

    private static final Logger LOG = LoggerFactory.getLogger(PersonMasterDataExternalService.class);

    private final PersonMasterDataService personMasterDataService;

    private final EthnicityMapper ethnicityMapper;

    public PersonMasterDataExternalServiceImpl(PersonMasterDataService personMasterDataService,
                                               EthnicityMapper ethnicityMapper) {
        this.personMasterDataService = personMasterDataService;
        this.ethnicityMapper = ethnicityMapper;
    }

    @Override
    public void updateActiveEthnicities(List<EthnicityDto> activeEthnicities) {
        LOG.debug("Input parameter -> activeEthnicities size = {}", activeEthnicities.size());
        LOG.trace("Input parameter -> activeEthnicities {}", activeEthnicities);
        personMasterDataService.updateActiveEthnicities(ethnicityMapper.fromEthnicityDtoList(activeEthnicities));
        LOG.debug("No output");
    }

}
