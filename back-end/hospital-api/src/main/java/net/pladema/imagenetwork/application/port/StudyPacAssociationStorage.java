package net.pladema.imagenetwork.application.port;

import java.util.Optional;

import net.pladema.imagenetwork.domain.ErrorDownloadStudyBo;
import net.pladema.imagenetwork.domain.PacsListBo;
import net.pladema.imagenetwork.domain.StudyPacBo;

public interface StudyPacAssociationStorage {

	PacsListBo getPacServersBy(String studyInstanceUID);

	Optional<String> saveStudyPacAssociation(StudyPacBo studyPacBo);

	Optional<Integer> saveErrorDownloadStudy(ErrorDownloadStudyBo errorDownloadStudyBo);
}
