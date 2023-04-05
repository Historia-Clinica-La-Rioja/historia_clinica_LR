package net.pladema.imagenetwork.application.port;

import net.pladema.imagenetwork.infrastructure.output.entity.StudyPacAssociation;

import java.util.Optional;

public interface StudyStorage {

	Optional<StudyPacAssociation> getStudyPacAssociation(String studyInstanceUID);
}
