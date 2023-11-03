package ar.lamansys.sgh.publicapi.imagenetwork.application.check;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.publicapi.imagenetwork.application.check.exceptions.CheckStudyAccessDeniedException;
import ar.lamansys.sgh.publicapi.imagenetwork.application.check.exceptions.BadStudyTokenException;
import ar.lamansys.sgh.publicapi.imagenetwork.infrastructure.input.service.ImageNetworkPublicApiPermissions;
import ar.lamansys.sgh.shared.infrastructure.input.service.imagenetwork.SharedStudyPermissionPort;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CheckStudyPermission {
	private final SharedStudyPermissionPort sharedStudyPermissionPort;
	private final ImageNetworkPublicApiPermissions imageNetworkPublicApiPermissions;

	public String run(String studyInstanceUID, String tokenStudy) throws BadStudyTokenException {

		if (!imageNetworkPublicApiPermissions.canAccess()) {
			throw new CheckStudyAccessDeniedException();
		}

		try {
			return sharedStudyPermissionPort.checkTokenStudyPermissions(studyInstanceUID, tokenStudy);
		} catch (Exception e) {
			throw new BadStudyTokenException(e.getMessage(), e);
		}
	}
}
