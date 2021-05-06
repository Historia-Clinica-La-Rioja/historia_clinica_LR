package net.pladema.person.service;

import net.pladema.person.repository.entity.Gender;
import net.pladema.person.repository.entity.IdentificationType;
import net.pladema.person.service.domain.EthnicityBo;

import java.util.List;
import java.util.Optional;

public interface PersonMasterDataService {

    Optional<Gender> getGender(Short genderId);

    List<Gender> getGenders();

    List<IdentificationType> getIdentificationTypes();

    Optional<IdentificationType> getIdentificationType(Short identificationTypeId);

    List<EthnicityBo> getActiveEthnicities();

    void updateActiveEthnicities(List<EthnicityBo> newActiveEthnicities);
}
