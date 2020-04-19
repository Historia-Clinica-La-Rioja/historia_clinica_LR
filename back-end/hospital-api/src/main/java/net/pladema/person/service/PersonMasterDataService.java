package net.pladema.person.service;

import net.pladema.person.repository.entity.Gender;
import org.hibernate.annotations.Cache;

import java.util.Optional;

public interface PersonMasterDataService {

    Optional<Gender> getGender(Short genderId);
}
