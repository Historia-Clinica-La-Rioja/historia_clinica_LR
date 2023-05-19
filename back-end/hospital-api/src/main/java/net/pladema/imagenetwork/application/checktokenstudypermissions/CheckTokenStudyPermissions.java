package net.pladema.imagenetwork.application.checktokenstudypermissions;

import ar.lamansys.sgx.shared.token.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import net.pladema.imagenetwork.application.exception.StudyException;
import net.pladema.imagenetwork.application.exception.StudyExceptionEnum;
import net.pladema.imagenetwork.application.port.StudyPermissionUUIDStorage;
import net.pladema.imagenetwork.domain.StudyPermissionBo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class CheckTokenStudyPermissions {

	private final String secretJWT;
	private final StudyPermissionUUIDStorage studyPermissionUUIDStorage;

	public CheckTokenStudyPermissions(@Value("${token.secret}") String secretJWT, StudyPermissionUUIDStorage studyPermissionUUIDStorage) {
		this.secretJWT = secretJWT;
		this.studyPermissionUUIDStorage = studyPermissionUUIDStorage;
	}

	public String run(String studyInstanceUID, String tokenStudy) {
		log.trace("Input -> studyInstanceUID '{}' tokenStudy '{}'", studyInstanceUID, tokenStudy);
		log.debug("Check Token study permission -> studyInstanceUID '{}'", studyInstanceUID);
		StudyPermissionBo studyPermissionFromToken = (isUUID(tokenStudy) ?
				processUUID(tokenStudy) :
				processJWT(tokenStudy, studyInstanceUID))
				.orElseThrow(() -> new StudyException(StudyExceptionEnum.TOKEN_INVALID, String.format(StudyExceptionEnum.TOKEN_INVALID.getMessage(), tokenStudy, studyInstanceUID)));

		if (!studyPermissionFromToken.getStudyInstanceUID().equals(studyInstanceUID))
			throw new StudyException(StudyExceptionEnum.TOKEN_INVALID, String.format(StudyExceptionEnum.TOKEN_INVALID.getMessage(), tokenStudy, studyInstanceUID));

		String token = studyPermissionFromToken.getToken();
		log.trace("Output -> token '{}' is valid", token);
		return token;
	}

	private static boolean isUUID(String tokenStudy) {
		try {
			UUID.fromString(tokenStudy);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	private Optional<StudyPermissionBo> processJWT(String tokenStudy, String studyInstanceUID) {
		return JWTUtils.parseClaims(tokenStudy, secretJWT)
				.filter(claims -> claims.containsKey("studyInstanceUID"))
				.map(claims -> Optional.of(new StudyPermissionBo(tokenStudy, (String) claims.get("studyInstanceUID"))))
				.orElseThrow(() -> new StudyException(StudyExceptionEnum.TOKEN_INVALID, String.format(StudyExceptionEnum.TOKEN_INVALID.getMessage(), tokenStudy, studyInstanceUID)));
	}

	private Optional<StudyPermissionBo> processUUID(String tokenStudy) {
		Optional<StudyPermissionBo> permission = studyPermissionUUIDStorage.getStudyPermission(tokenStudy);
		if (permission.isPresent() && permission.get().expired()) {
			studyPermissionUUIDStorage.removeStudyPermission(permission.get());
			return Optional.empty();
		}
		return permission;
	}
}
