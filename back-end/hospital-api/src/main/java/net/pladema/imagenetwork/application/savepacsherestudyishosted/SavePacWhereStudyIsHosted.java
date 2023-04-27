package net.pladema.imagenetwork.application.savepacsherestudyishosted;

import net.pladema.imagenetwork.application.exception.StudyException;

import net.pladema.imagenetwork.application.exception.StudyExceptionEnum;

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
		log.debug("Save PAC URL {} where the study {} is hosted", studyPacBo.getPacGlobalURL(), studyPacBo.getStudyInstanceUID());
		String study = studyStorage.saveStudyPacAssociation(studyPacBo)
				.orElseThrow(() -> new StudyException(StudyExceptionEnum.PAC_SERVER_NOT_FOUND, String.format(StudyExceptionEnum.PAC_SERVER_NOT_FOUND.getMessage(), studyPacBo.getPacGlobalURL())));
		log.debug("Output -> study {} and pac-host registered", study);
		return study;
	}
}
