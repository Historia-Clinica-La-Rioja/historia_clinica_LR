package net.pladema.person.service.impl;

import net.pladema.person.repository.EducationLevelRepository;
import net.pladema.person.repository.EthnicityRepository;
import net.pladema.person.repository.GenderRepository;
import net.pladema.person.repository.IdentificationTypeRepository;
import net.pladema.person.repository.OccupationRepository;
import net.pladema.person.repository.SelfPerceivedGenderRepository;
import net.pladema.person.repository.entity.Ethnicity;
import net.pladema.person.repository.entity.Gender;
import net.pladema.person.repository.entity.IdentificationType;
import net.pladema.person.repository.entity.SelfPerceivedGender;
import net.pladema.person.service.PersonMasterDataService;
import net.pladema.person.service.domain.EducationLevelBo;
import net.pladema.person.service.domain.EthnicityBo;
import net.pladema.person.service.domain.PersonOccupationBo;
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

    private final SelfPerceivedGenderRepository selfPerceivedGenderRepository;

    private final IdentificationTypeRepository identificationTypeRepository;

    private final EthnicityRepository ethnicityRepository;

    private final EducationLevelRepository educationLevelRepository;

    private final OccupationRepository occupationRepository;

    public PersonMasterDataServiceImpl(GenderRepository genderRepository,
                                       SelfPerceivedGenderRepository selfPerceivedGenderRepository, IdentificationTypeRepository identificationTypeRepository,
                                       EthnicityRepository ethnicityRepository, EducationLevelRepository educationLevelRepository, OccupationRepository occupationRepository) {
        super();
        this.genderRepository = genderRepository;
        this.selfPerceivedGenderRepository = selfPerceivedGenderRepository;
        this.identificationTypeRepository = identificationTypeRepository;
        this.ethnicityRepository = ethnicityRepository;
        this.educationLevelRepository = educationLevelRepository;
        this.occupationRepository = occupationRepository;
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
	public Optional<String> getSelfPerceivedGenderById(Short selfPerceivedGenderId) {
		LOG.debug("Input parameter -> {}", selfPerceivedGenderId);
		Optional<String> result = Optional.empty();
		if (selfPerceivedGenderId != null) {
			result = selfPerceivedGenderRepository.findById(selfPerceivedGenderId).map(selfGender -> selfGender.getDescription());
		}
			LOG.debug("Output -> {}", result);
		return result;
	}

	@Override
    public List<Gender> getGenders() {
        return genderRepository.findAll();
    }

    @Override
    public List<SelfPerceivedGender> getSelfPerceivedGender() {
        return selfPerceivedGenderRepository.getSelfPerceivedGenderOrderByOrden();
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

    @Override
    public List<EducationLevelBo> getActiveEducationLevels() {
        LOG.debug("No input parameters");
        List<EducationLevelBo> result = educationLevelRepository.findAllActive().stream()
                .map(EducationLevelBo::new)
                .collect(Collectors.toList());
        LOG.debug("Output size -> {}", result.size());
        LOG.trace("Output -> {}", result);
        return result;
    }

    @Override
    public List<PersonOccupationBo> getActiveOccupations() {
        LOG.debug("No input parameters");
        List<PersonOccupationBo> result = occupationRepository.findAllActive().stream()
                .map(PersonOccupationBo::new)
                .collect(Collectors.toList());
        LOG.debug("Output size -> {}", result.size());
        LOG.trace("Output -> {}", result);
        return result;
    }

    @Override
    public void updateActiveEthnicities(List<EthnicityBo> newActiveEthnicities) {
        LOG.debug("Input parameter -> newActiveEthnicities size = {}", newActiveEthnicities.size());
        LOG.trace("Input parameter -> newActiveEthnicities {}", newActiveEthnicities);
        List<EthnicityBo> actualActiveEthnicities = this.getActiveEthnicities();
        List<Ethnicity> ethnicitiesToSave = getNonActiveEthnicitiesToSave(newActiveEthnicities, actualActiveEthnicities);
        ethnicitiesToSave.addAll(getNewActiveEthnicitiesToSave(newActiveEthnicities, actualActiveEthnicities));
        ethnicityRepository.saveAll(ethnicitiesToSave);
        LOG.debug("Finished updating ethnicities");
    }

    private List<Ethnicity> getNewActiveEthnicitiesToSave(List<EthnicityBo> newActiveEthnicities, List<EthnicityBo> actualActiveEthnicities) {
        LOG.debug("Input parameters -> newActiveEthnicities size = {}, actualActiveEthnicities size = {}", newActiveEthnicities.size(), actualActiveEthnicities.size());
        LOG.trace("Input parameters -> newActiveEthnicities {}, actualActiveEthnicities {}", newActiveEthnicities, actualActiveEthnicities);
        List<Ethnicity> result = newActiveEthnicities.stream()
                .filter(newEthnicity -> ethnicityIsNotContained(actualActiveEthnicities, newEthnicity))
                .map(ethnicityBo -> {
                    ethnicityBo.setId(ethnicityRepository.findIdBySctidAndPt(ethnicityBo.getSctid(), ethnicityBo.getPt()).orElse(null));
                    ethnicityBo.setActive(Boolean.TRUE);
                    return new Ethnicity(ethnicityBo);
                })
                .collect(Collectors.toList());
        LOG.debug("Output size -> {}", result.size());
        LOG.trace("Output -> {}", result);
        return result;
    }

    private List<Ethnicity> getNonActiveEthnicitiesToSave(List<EthnicityBo> newActiveEthnicities, List<EthnicityBo> actualActiveEthnicities) {
        LOG.debug("Input parameters -> newActiveEthnicities size = {}, actualActiveEthnicities size = {}", newActiveEthnicities.size(), actualActiveEthnicities.size());
        LOG.trace("Input parameters -> newActiveEthnicities {}, actualActiveEthnicities {}", newActiveEthnicities, actualActiveEthnicities);
        List<Ethnicity> result =  actualActiveEthnicities.stream()
                .filter(actualEthnicity -> ethnicityIsNotContained(newActiveEthnicities, actualEthnicity))
                .map(ethnicityBo -> {
                    ethnicityBo.setActive(Boolean.FALSE);
                    return new Ethnicity(ethnicityBo); })
                .collect(Collectors.toList());
        LOG.debug("Output size -> {}", result.size());
        LOG.trace("Output -> {}", result);
        return result;
    }

    private boolean ethnicityIsNotContained(List<EthnicityBo> ethnicityBoList, EthnicityBo ethnicity) {
        LOG.debug("Input parameters -> ethnicityBoList size = {}, ethnicity {}", ethnicityBoList.size(), ethnicity);
        LOG.trace("Input parameters -> ethnicityBoList {}, ethnicity {}", ethnicityBoList, ethnicity);
        boolean result = ethnicityBoList.stream()
                .noneMatch(newEthnicity ->
                        (ethnicity.getSctid().equals(newEthnicity.getSctid())
                        && ethnicity.getPt().equals(newEthnicity.getPt())));
        LOG.debug("Output -> {}", result);
        return result;
    }
}
