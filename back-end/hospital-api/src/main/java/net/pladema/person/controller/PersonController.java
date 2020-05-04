package net.pladema.person.controller;

import io.swagger.annotations.Api;
import net.pladema.address.controller.dto.AddressDto;
import net.pladema.address.controller.service.AddressExternalService;
import net.pladema.person.controller.dto.APersonDto;
import net.pladema.person.controller.dto.BMPersonDto;
import net.pladema.person.controller.dto.PersonalInformationDto;
import net.pladema.person.controller.mapper.PersonMapper;
import net.pladema.person.controller.mock.MocksPerson;
import net.pladema.person.repository.domain.PersonalInformation;
import net.pladema.person.repository.entity.Person;
import net.pladema.person.repository.entity.PersonExtended;
import net.pladema.person.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/person")
@Api(value = "Person", tags = { "Person" })
public class PersonController {

    private static final Logger LOG = LoggerFactory.getLogger(PersonController.class);

    private final PersonService personService;

    private final AddressExternalService addressExternalService;

    private final PersonMapper personMapper;


    public PersonController(PersonService personService, PersonMapper personMapper, AddressExternalService addressExternalService) {
        super();
        this.personService = personService;
        this.personMapper = personMapper;
        this.addressExternalService = addressExternalService;
    }
    
    @PostMapping
    @Transactional
    public ResponseEntity<BMPersonDto> addPerson(
            @RequestBody APersonDto personDto) throws URISyntaxException {

        Person personToAdd = personMapper.fromPersonDto(personDto);
        LOG.debug("Going to add person -> {}", personToAdd);
        Person createdPerson = personService.addPerson(personToAdd);

        AddressDto addressToAdd = personMapper.updatePersonAddress(personDto);
        LOG.debug("Going to add address -> {}", addressToAdd);
        addressToAdd = addressExternalService.addAddress(addressToAdd);

        PersonExtended personExtendedtoAdd = personMapper.updatePersonExtended(personDto, addressToAdd.getId());
        personExtendedtoAdd.setId(createdPerson.getId());
        LOG.debug("Going to add person extended -> {}", personExtendedtoAdd);
        personService.addPersonExtended(personExtendedtoAdd);

        return ResponseEntity.created(new URI("")).body(personMapper.fromPerson(createdPerson));
    }

    @GetMapping(value = "/minimalsearch")
    public ResponseEntity<List<Integer>> getPersonMinimal(
            @RequestParam(value = "identificationTypeId", required = true) Short identificationTypeId,
            @RequestParam(value = "identificationNumber", required = true) String identificationNumber,
            @RequestParam(value = "genderId", required = true) Short genderId){
        LOG.debug("Input data -> identificationTypeId {}, identificationNumber {}, genderId {}", identificationTypeId,
                identificationNumber, genderId);
        List<Integer> result = personService.getPersonByDniAndGender(identificationTypeId,identificationNumber, genderId);
        LOG.debug("Ids resultantes -> {}", result);
        return  ResponseEntity.ok().body(result);
    }

    @GetMapping("/{personId}/personalInformation")
    public ResponseEntity<PersonalInformationDto> getPersonalInformation(@PathVariable(name = "personId") Integer personId){
        LOG.debug("Input parameters -> {}", personId);
        PersonalInformationDto result;
        try {
            PersonalInformation personalInformation = personService.getPersonalInformation(personId)
                    .orElseThrow(() -> new EntityNotFoundException("person.invalid"));
            result = personMapper.fromPersonalInformation(personalInformation);
        } catch (Exception e) {
            LOG.error("Error",e);
            result = MocksPerson.mockPersonalInformation(personId);
        }
        LOG.debug("Output -> {}", result);
        return  ResponseEntity.ok().body(result);
    }
}