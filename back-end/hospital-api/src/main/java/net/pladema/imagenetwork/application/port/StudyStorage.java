package net.pladema.imagenetwork.application.port;

import net.pladema.imagenetwork.domain.StudyPacBo;
import net.pladema.imagenetwork.infrastructure.output.entity.StudyInformation;

import java.util.Optional;

public interface StudyStorage {

	Optional<StudyInformation> getStudyPacAssociation(String studyInstanceUID);

	Optional<String> saveStudyPacAssociation(StudyPacBo studyPacBo);
}
