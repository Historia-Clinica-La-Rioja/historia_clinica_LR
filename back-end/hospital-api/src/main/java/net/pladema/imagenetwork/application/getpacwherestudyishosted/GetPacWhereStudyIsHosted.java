package net.pladema.imagenetwork.application.getpacwherestudyishosted;

import java.net.MalformedURLException;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.establishment.repository.entity.PacServer;
import net.pladema.imagenetwork.application.exception.StudyException;
import net.pladema.imagenetwork.application.exception.StudyExceptionEnum;
import net.pladema.imagenetwork.domain.PacsListBo;
import net.pladema.imagenetwork.infrastructure.output.database.StudyPacAssociationStorageImpl;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetPacWhereStudyIsHosted {

	private final StudyPacAssociationStorageImpl studyStorage;

	public PacsListBo run(String studyInstanceUID) throws MalformedURLException {
		log.debug("Get PAC URL where the study {} is hosted", studyInstanceUID);
		List<PacServer> pacServers = studyStorage.getPacServersBy(studyInstanceUID);
		if (pacServers.isEmpty())
			throw new StudyException(StudyExceptionEnum.STUDYINSTANCEUID_NOT_FOUND, String.format(StudyExceptionEnum.STUDYINSTANCEUID_NOT_FOUND.getMessage(), studyInstanceUID));
		var result = new PacsListBo(pacServers);
		log.debug("Output -> PacsListBo {}", result);
		return result;
	}
}
