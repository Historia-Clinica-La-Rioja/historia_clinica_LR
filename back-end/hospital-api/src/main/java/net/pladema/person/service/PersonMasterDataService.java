package net.pladema.person.service;

import net.pladema.person.repository.entity.Gender;
import net.pladema.person.repository.entity.IdentificationType;
import net.pladema.person.repository.entity.SelfPerceivedGender;
import net.pladema.person.service.domain.EducationLevelBo;
import net.pladema.person.service.domain.EthnicityBo;
import net.pladema.person.service.domain.PersonOccupationBo;

import java.util.List;
import java.util.Optional;

public interface PersonMasterDataService {

    Optional<Gender> getGender(Short genderId);

	Optional<String> getSelfPerceivedGenderById(Short selfPerceivedGenderId);

    List<Gender> getGenders();

    List<SelfPerceivedGender> getSelfPerceivedGender();

    List<IdentificationType> getIdentificationTypes();

    Optional<IdentificationType> getIdentificationType(Short identificationTypeId);

    List<EthnicityBo> getActiveEthnicities();

    List<EducationLevelBo> getActiveEducationLevels();

    List<PersonOccupationBo> getActiveOccupations();

    void updateActiveEthnicities(List<EthnicityBo> newActiveEthnicities);
}
