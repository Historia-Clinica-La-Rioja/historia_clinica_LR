package net.pladema.person.service.impl;

import net.pladema.person.repository.GenderRepository;
import net.pladema.person.repository.PersonExtendedRepository;
import net.pladema.person.repository.PersonRepository;
import net.pladema.person.repository.domain.PersonalInformation;
import net.pladema.person.repository.entity.Gender;
import net.pladema.person.repository.entity.Person;
import net.pladema.person.repository.entity.PersonExtended;
import net.pladema.person.service.PersonMasterDataService;
import net.pladema.person.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class PersonMasterDataServiceImpl implements PersonMasterDataService {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final GenderRepository genderRepository;

    public PersonMasterDataServiceImpl(GenderRepository genderRepository) {
        super();
        this.genderRepository = genderRepository;
    }

    @Override
    public Optional<Gender> getGender(Short genderId) {
        LOG.debug("Input parameter -> {}", genderId);
        Optional<Gender> result = genderRepository.findById(genderId);
        LOG.debug("Output -> {}", result);
        return result;
    }
}
