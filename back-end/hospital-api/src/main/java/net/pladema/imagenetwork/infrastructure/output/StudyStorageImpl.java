package net.pladema.imagenetwork.infrastructure.output;

import java.util.HashSet;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.establishment.repository.PacServerRepository;
import net.pladema.establishment.repository.entity.PacServer;
import net.pladema.imagenetwork.application.port.StudyStorage;
import net.pladema.imagenetwork.domain.StudyPacBo;
import net.pladema.imagenetwork.infrastructure.output.entity.StudyInformation;
import net.pladema.imagenetwork.infrastructure.output.repository.StudyPacAssociationRepository;


@AllArgsConstructor
@Slf4j
@Repository
public class StudyStorageImpl implements StudyStorage {

	private final StudyPacAssociationRepository studyPacRepository;
	private final PacServerRepository pacServerRepository;

	@Override
	public Optional<StudyInformation> getStudyPacAssociation(String studyInstanceUID) {
		return studyPacRepository.findById(studyInstanceUID);
	}

	@Override
	public Optional<String> saveStudyPacAssociation(StudyPacBo studyPacBo) {
		PacServer pacServerToSearch = new PacServer();
		pacServerToSearch.setDomain(studyPacBo.getDomain());
		return pacServerRepository.findOne(Example.of(pacServerToSearch)).map(pac -> {
			StudyInformation association = getStudyPacAssociation(studyPacBo.getStudyInstanceUID())
					.orElseGet(() -> new StudyInformation(studyPacBo.getStudyInstanceUID(), new HashSet<>()));

			association.getPacs().add(pac);

			return studyPacRepository.save(association).getStudyInstanceUID();
		});
	}
}
