package net.pladema.imagenetwork.infrastructure.output;

import net.pladema.imagenetwork.infrastructure.output.entity.StudyPacAssociation;
import net.pladema.imagenetwork.infrastructure.output.repository.StudyPacAssociationRepository;

import org.springframework.stereotype.Repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.imagenetwork.application.port.StudyStorage;

import java.util.Optional;


@AllArgsConstructor
@Slf4j
@Repository
public class StudyStorageImpl implements StudyStorage {

	private final StudyPacAssociationRepository studyPacRepository;

	@Override
	public Optional<StudyPacAssociation> getStudyPacAssociation(String studyInstanceUID) {
		return studyPacRepository.findById(studyInstanceUID);
	}
}
