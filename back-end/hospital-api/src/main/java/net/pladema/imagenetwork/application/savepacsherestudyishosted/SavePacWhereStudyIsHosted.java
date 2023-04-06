package net.pladema.imagenetwork.application.savepacsherestudyishosted;

import net.pladema.imagenetwork.application.exception.StudyException;

import net.pladema.imagenetwork.application.exception.StudyExceptionEnum;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.imagenetwork.domain.StudyInfoBo;
import net.pladema.imagenetwork.infrastructure.output.StudyStorageImpl;

@Service
@RequiredArgsConstructor
@Slf4j
public class SavePacWhereStudyIsHosted {

	private final StudyStorageImpl studyStorage;

	public String run(StudyInfoBo studyInfoBo) {
		log.debug("Save PAC URL {} where the study {} is hosted", studyInfoBo.getPacGlobalURL(), studyInfoBo.getStudyInstanceUID());
		String study = studyStorage.saveStudyPacAssociation(studyInfoBo)
				.orElseThrow(() -> new StudyException(StudyExceptionEnum.PAC_SERVER_NOT_FOUND, String.format(StudyExceptionEnum.PAC_SERVER_NOT_FOUND.getMessage(), studyInfoBo.getPacGlobalURL())));
		log.debug("Output -> study {} and pac-host registered", study);
		return study;
	}
}
