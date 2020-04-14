package net.pladema.person.controller;

import io.swagger.annotations.Api;
import net.pladema.address.repository.entity.Address;
import net.pladema.person.controller.dto.APersonDto;
import net.pladema.person.controller.dto.BMPersonDto;
import net.pladema.person.controller.mapper.PersonMapper;
import net.pladema.person.repository.entity.Person;
import net.pladema.person.repository.entity.PersonExtended;
import net.pladema.person.service.AddressExternalService;
import net.pladema.person.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/person")
@Api(value = "Person", tags = { "Person" })
public class PersonController {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

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
    public ResponseEntity<BMPersonDto> addPerson(
            @RequestBody APersonDto personDto) throws URISyntaxException {

        Person personToAdd = personMapper.fromPersonDto(personDto);
        LOG.debug("Going to add person -> {}", personToAdd);
        Person createdPerson = personService.addPerson(personToAdd);

        Address addressToAdd = personMapper.updatePersonAddress(personDto);
        LOG.debug("Going to add address -> {}", addressToAdd);
        Address createdAddress = addressExternalService.addAddress(addressToAdd);


        PersonExtended personExtendedtoAdd = personMapper.updatePersonExtended(personDto, createdAddress.getId());
        personExtendedtoAdd.setId(createdPerson.getId());
        LOG.debug("Going to add person extended -> {}", personExtendedtoAdd);
        PersonExtended createdPersonExtended = personService.addPersonExtended(personExtendedtoAdd);

        return ResponseEntity.created(new URI("")).body(personMapper.fromPerson(createdPerson));
    }
}