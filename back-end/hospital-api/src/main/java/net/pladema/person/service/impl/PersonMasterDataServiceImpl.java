package net.pladema.person.service.impl;

import net.pladema.person.repository.EthnicityRepository;
import net.pladema.person.repository.GenderRepository;
import net.pladema.person.repository.IdentificationTypeRepository;
import net.pladema.person.repository.entity.Gender;
import net.pladema.person.repository.entity.IdentificationType;
import net.pladema.person.service.PersonMasterDataService;
import net.pladema.person.service.domain.EthnicityBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersonMasterDataServiceImpl implements PersonMasterDataService {

    private static final Logger LOG = LoggerFactory.getLogger(PersonMasterDataServiceImpl.class);

    private final GenderRepository genderRepository;

    private final IdentificationTypeRepository identificationTypeRepository;

    private final EthnicityRepository ethnicityRepository;

    public PersonMasterDataServiceImpl(GenderRepository genderRepository,
                                       IdentificationTypeRepository identificationTypeRepository,
                                       EthnicityRepository ethnicityRepository) {
        super();
        this.genderRepository = genderRepository;
        this.identificationTypeRepository = identificationTypeRepository;
        this.ethnicityRepository = ethnicityRepository;
        LOG.debug("{}", "created service");
    }

    @Override
    public Optional<Gender> getGender(Short genderId) {
        LOG.debug("Input parameter -> {}", genderId);
        Optional<Gender> result = Optional.empty();
        if (genderId != null)
            result = genderRepository.findById(genderId);
        LOG.debug("Output -> {}", result);
        return result;
    }

    @Override
    public List<Gender> getGenders() {
        return genderRepository.findAll();
    }

    @Override
    public List<IdentificationType> getIdentificationTypes() {
        return identificationTypeRepository.findAll();
    }

    @Override
    public Optional<IdentificationType> getIdentificationType(Short identificationTypeId) {
        LOG.debug("Input parameter -> {}", identificationTypeId);
        Optional<IdentificationType> result = Optional.empty();
        if (identificationTypeId != null)
            result = identificationTypeRepository.findById(identificationTypeId);
        LOG.debug("Output -> {}", result);
        return result;
    }

    @Override
    public List<EthnicityBo> getActiveEthnicities() {
        LOG.debug("No input parameters");
        List<EthnicityBo> result = ethnicityRepository.findAllActive().stream()
                .map(EthnicityBo::new)
                .collect(Collectors.toList());
        LOG.debug("Output size -> {}", result.size());
        LOG.trace("Output -> {}", result);
        return result;
    }
}
