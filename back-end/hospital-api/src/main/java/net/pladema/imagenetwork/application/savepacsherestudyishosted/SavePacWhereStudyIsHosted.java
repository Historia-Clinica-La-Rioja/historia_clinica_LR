package net.pladema.imagenetwork.application.savepacsherestudyishosted;

import net.pladema.imagenetwork.application.exception.StudyException;

import net.pladema.imagenetwork.domain.exception.EStudyException;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.imagenetwork.domain.StudyPacBo;
import net.pladema.imagenetwork.infrastructure.output.database.StudyPacAssociationStorageImpl;

@Service
@RequiredArgsConstructor
@Slf4j
public class SavePacWhereStudyIsHosted {

	private final StudyPacAssociationStorageImpl studyStorage;

	public String run(StudyPacBo studyPacBo) {
		log.debug("Save PAC Server {} where the study {} is hosted", studyPacBo.getPacServerId(), studyPacBo.getStudyInstanceUID());
		String study = studyStorage.saveStudyPacAssociation(studyPacBo)
				.orElseThrow(() -> new StudyException(EStudyException.PAC_SERVER_NOT_FOUND, "app.imagenetwork.error.pacs-not-found"));
		log.debug("Output -> study {} and pac-host registered", study);
		return study;
	}
}
