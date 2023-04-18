package net.pladema.imagenetwork.application.getpacwherestudyishosted;

import net.pladema.imagenetwork.domain.PacsListBo;
import net.pladema.imagenetwork.infrastructure.output.entity.StudyInformation;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.imagenetwork.application.exception.StudyException;
import net.pladema.imagenetwork.application.exception.StudyExceptionEnum;
import net.pladema.imagenetwork.infrastructure.output.StudyStorageImpl;

import java.net.MalformedURLException;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetPacWhereStudyIsHosted {

	private final StudyStorageImpl studyStorage;

	public PacsListBo run(String studyInstanceUID) throws MalformedURLException {
		log.debug("Get PAC URL where the study {} is hosted", studyInstanceUID);
		StudyInformation association = studyStorage.getStudyPacAssociation(studyInstanceUID)
				.orElseThrow(() -> new StudyException(StudyExceptionEnum.STUDYINSTANCEUID_NOT_FOUND, String.format(StudyExceptionEnum.STUDYINSTANCEUID_NOT_FOUND.getMessage(), studyInstanceUID)));
		var result = new PacsListBo(association);
		log.debug("Output -> PacsListBo {}", result);
		return result;
	}
}
