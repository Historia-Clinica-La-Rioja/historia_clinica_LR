package net.pladema.imagenetwork.infrastructure.output.database;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import net.pladema.imagenetwork.domain.ErrorDownloadStudyBo;
import net.pladema.imagenetwork.infrastructure.output.database.entity.ErrorDownloadStudy;
import net.pladema.imagenetwork.infrastructure.output.database.mapper.StudyStorageMapper;
import net.pladema.imagenetwork.infrastructure.output.database.repository.ErrorDownloadStudyRepository;
import net.pladema.imagenetwork.infrastructure.output.database.repository.StudyPacAssociationRepository;

import org.springframework.data.domain.Example;

import net.pladema.establishment.repository.PacServerRepository;
import net.pladema.establishment.repository.entity.PacServer;
import net.pladema.imagenetwork.application.port.StudyPacAssociationStorage;
import net.pladema.imagenetwork.domain.StudyPacBo;
import net.pladema.imagenetwork.infrastructure.output.database.entity.StudyPacAssociation;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StudyPacAssociationStorageImpl implements StudyPacAssociationStorage {

	private final StudyPacAssociationRepository studyPacRepository;
	private final PacServerRepository pacServerRepository;
	private final StudyStorageMapper studyStorageMapper;
	private final ErrorDownloadStudyRepository errorDownloadStudyRepository;

	@Override
	public List<PacServer> getPacServersBy(String studyInstanceUID) {
		return studyPacRepository.findAllPacServerBy(studyInstanceUID);
	}

	@Override
	public Optional<String> saveStudyPacAssociation(StudyPacBo studyPacBo) {
		PacServer pacServerToSearch = new PacServer();
		pacServerToSearch.setId(studyPacBo.getPacServerId());
		return pacServerRepository.findOne(Example.of(pacServerToSearch))
				.map(pac -> studyPacRepository.save(new StudyPacAssociation(studyPacBo.getStudyInstanceUID(), pac.getId())))
				.map(studyPacSaved -> studyPacBo.getStudyInstanceUID());
	}

	@Override
	public Optional<Integer> saveErrorDownloadStudy(ErrorDownloadStudyBo errorDownloadStudyBo) {
		var toSave = studyStorageMapper.toErrorDownloadStudy(errorDownloadStudyBo);
		return Optional.of(errorDownloadStudyRepository.save(toSave))
				.map(ErrorDownloadStudy::getId);
	}
}
