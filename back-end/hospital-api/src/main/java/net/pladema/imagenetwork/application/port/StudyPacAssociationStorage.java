package net.pladema.imagenetwork.application.port;

import java.util.List;
import java.util.Optional;

import net.pladema.establishment.repository.entity.PacServer;
import net.pladema.imagenetwork.domain.ErrorDownloadStudyBo;
import net.pladema.imagenetwork.domain.StudyPacBo;

public interface StudyPacAssociationStorage {

	List<PacServer> getPacServersBy(String studyInstanceUID);

	Optional<String> saveStudyPacAssociation(StudyPacBo studyPacBo);

	Optional<Integer> saveErrorDownloadStudy(ErrorDownloadStudyBo errorDownloadStudyBo);
}
