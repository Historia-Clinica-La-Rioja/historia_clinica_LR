package net.pladema.imagenetwork.application.generatetokenstudypermissions;

import lombok.extern.slf4j.Slf4j;

import net.pladema.imagenetwork.application.port.StudyPermissionUUIDStorage;
import net.pladema.imagenetwork.domain.StudyPermissionBo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
@Slf4j
public class GenerateStudyTokenUUID {

	private final StudyPermissionUUIDStorage studyPermissionUUIDStorage;
	private final Duration expirationTime;

	public GenerateStudyTokenUUID(StudyPermissionUUIDStorage studyPermissionUUIDStorage,
								  @Value("${app.imagenetwork.permission.expiration}") Duration expirationTime) {
		this.studyPermissionUUIDStorage = studyPermissionUUIDStorage;
		this.expirationTime = expirationTime;
	}

	public String run(String studyInstanceUID) {
		log.debug("Input -> studyInstanceUID '{}' expirationTime '{}'", studyInstanceUID, expirationTime);
		StudyPermissionBo studyPermissionBo = new StudyPermissionBo(UUID.randomUUID().toString(), studyInstanceUID, expirationTime);
		String result = studyPermissionUUIDStorage.saveStudyPermission(studyPermissionBo);
		log.debug("Output -> generated token UUID '{}'", result);
		return result;
	}
}
