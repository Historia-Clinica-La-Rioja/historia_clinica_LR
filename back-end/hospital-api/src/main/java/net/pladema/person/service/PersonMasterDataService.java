package net.pladema.person.service;

import net.pladema.person.repository.entity.Gender;
import net.pladema.person.repository.entity.IdentificationType;
import org.hibernate.annotations.Cache;

import java.util.List;
import java.util.Optional;

public interface PersonMasterDataService {

    Optional<Gender> getGender(Short genderId);

    public List<Gender> getGenders();

    public List<IdentificationType> getIdentificationTypes();
}
