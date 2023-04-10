package net.pladema.person.service;

import net.pladema.patient.repository.entity.EducationLevel;
import net.pladema.patient.repository.entity.Occupation;
import net.pladema.person.repository.entity.Ethnicity;
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
	Optional<Ethnicity> getEthnicityById(Integer ethnicityId);

    List<EducationLevelBo> getActiveEducationLevels();
	Optional<EducationLevel> getEducationLevelById(Integer educationLevelId);

    List<PersonOccupationBo> getActiveOccupations();
	Optional<Occupation> getOccupationById(Integer occupationId);

    void updateActiveEthnicities(List<EthnicityBo> newActiveEthnicities);
}
