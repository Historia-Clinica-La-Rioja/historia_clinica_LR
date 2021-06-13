package net.pladema.person.controller;

import io.swagger.annotations.Api;
import net.pladema.person.controller.dto.EducationLevelDto;
import net.pladema.person.controller.dto.EthnicityDto;
import net.pladema.person.controller.dto.GenderDto;
import net.pladema.person.controller.dto.IdentificationTypeDto;
import net.pladema.person.controller.dto.PersonOccupationDto;
import net.pladema.person.controller.mapper.EducationLevelMapper;
import net.pladema.person.controller.mapper.EthnicityMapper;
import net.pladema.person.controller.mapper.GenderMapper;
import net.pladema.person.controller.mapper.IdentificationTypeMapper;
import net.pladema.person.controller.mapper.OccupationMapper;
import net.pladema.person.repository.entity.Gender;
import net.pladema.person.repository.entity.IdentificationType;
import net.pladema.person.service.PersonMasterDataService;
import net.pladema.person.service.domain.EducationLevelBo;
import net.pladema.person.service.domain.EthnicityBo;
import net.pladema.person.service.domain.PersonOccupationBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/person/masterdata")
@Api(value = "Person Master Data", tags = { "Person Master Data" })
public class PersonMasterDataController {

    private static final Logger LOG = LoggerFactory.getLogger(PersonMasterDataController.class);
    public static final String OUTPUT = "Output -> {}";

    private final PersonMasterDataService personMasterDataService;

    private final GenderMapper genderMapper;

    private final IdentificationTypeMapper identificationTypeMapper;

    private final EthnicityMapper ethnicityMapper;

    private final EducationLevelMapper educationLevelMapper;

    private final OccupationMapper occupationMapper;

    public PersonMasterDataController(PersonMasterDataService personMasterDataService,
                                      GenderMapper genderMapper,
                                      IdentificationTypeMapper identificationTypeMapper,
                                      EthnicityMapper ethnicityMapper, EducationLevelMapper educationLevelMapper, OccupationMapper occupationMapper) {
        super();
        this.personMasterDataService = personMasterDataService;
        this.genderMapper = genderMapper;
        this.identificationTypeMapper = identificationTypeMapper;
        this.ethnicityMapper = ethnicityMapper;
        this.educationLevelMapper = educationLevelMapper;
        this.occupationMapper = occupationMapper;
    }

    @GetMapping(value = "/genders")
    public ResponseEntity<Collection<GenderDto>> getGenders() {
        LOG.debug("{}", "All genders");
        List<Gender> genders = personMasterDataService.getGenders();
        List<GenderDto> result = genderMapper.fromGenderList(genders);
        LOG.debug(OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(value = "/identificationTypes")
    public ResponseEntity<Collection<IdentificationTypeDto>> getIdentificationTypes() {
        LOG.debug("{}", "All identificationTypes");
        List<IdentificationType> identificationTypes = personMasterDataService.getIdentificationTypes();
        List<IdentificationTypeDto> result = identificationTypeMapper.fromIdentificationTypeList(identificationTypes);
        LOG.debug(OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(value = "/ethnicities")
    public ResponseEntity<Collection<EthnicityDto>> getEthnicities() {
        LOG.debug("{}", "All ethnicities");
        List<EthnicityBo> ethnicities = personMasterDataService.getActiveEthnicities();
        List<EthnicityDto> result = ethnicityMapper.fromEthnicityBoList(ethnicities);
        LOG.debug("Output size -> {}", result.size());
        LOG.trace(OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(value = "/educationLevel")
    public ResponseEntity<Collection<EducationLevelDto>> getEducationLevels(){
        LOG.debug("{}", "All education levels");
        List<EducationLevelBo> educations = personMasterDataService.getActiveEducationLevels();
        List<EducationLevelDto> result = educationLevelMapper.fromEducationBoList(educations);
        LOG.debug("Output size -> {}", result.size());
        LOG.trace(OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(value = "/occupations")
    public ResponseEntity<Collection<PersonOccupationDto>> getOccupations(){
        LOG.debug("{}", "All education levels");
        List<PersonOccupationBo> occupations = personMasterDataService.getActiveOccupations();
        List<PersonOccupationDto> result = occupationMapper.fromOccupationBoList(occupations);
        LOG.debug("Output size -> {}", result.size());
        LOG.trace(OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }
}
