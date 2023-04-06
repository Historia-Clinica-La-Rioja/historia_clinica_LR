package net.pladema.imagenetwork.infrastructure.output;

import net.pladema.establishment.repository.PacServerRepository;
import net.pladema.establishment.repository.entity.PacServer;
import net.pladema.imagenetwork.domain.StudyInfoBo;
import net.pladema.imagenetwork.infrastructure.output.entity.StudyPacAssociation;
import net.pladema.imagenetwork.infrastructure.output.repository.StudyPacAssociationRepository;

import org.springframework.data.domain.Example;
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
	private final PacServerRepository pacServerRepository;

	@Override
	public Optional<StudyPacAssociation> getStudyPacAssociation(String studyInstanceUID) {
		return studyPacRepository.findById(studyInstanceUID);
	}

	@Override
	public Optional<String> saveStudyPacAssociation(StudyInfoBo studyInfoBo) {
		PacServer pacServerToSearch = new PacServer();
		pacServerToSearch.setDomain(studyInfoBo.getDomain());

		return pacServerRepository.findOne(Example.of(pacServerToSearch)).map(pac -> {
			StudyPacAssociation s = new StudyPacAssociation(studyInfoBo.getStudyInstanceUID(), pac);
			return studyPacRepository.save(s).getStudyInstanceUID();
		});
	}
}
