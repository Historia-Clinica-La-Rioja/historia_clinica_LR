package ar.lamansys.sgh.publicapi.imagecenter.application.updateresult;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.publicapi.imagecenter.application.updateresult.exceptions.UpdateMoveStudyAccessDeniedException;
import ar.lamansys.sgh.publicapi.imagecenter.application.updateresult.exceptions.UpdateResultException;
import ar.lamansys.sgh.publicapi.imagecenter.infrastructure.input.service.ImageCenterPublicApiPermissions;
import ar.lamansys.sgh.shared.infrastructure.input.service.imagenetwork.SharedLoadStudiesResultPort;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UpdateResult {

	public final ImageCenterPublicApiPermissions imageCenterPublicApiPermissions;
	public final SharedLoadStudiesResultPort moveStudiesService;

	public Boolean run(Integer idMove, String status, String result) throws UpdateResultException {
		var studyInstitutionId = moveStudiesService.findInstitutionId(idMove)
				.orElseThrow(UpdateMoveStudyAccessDeniedException::new);

		if (!imageCenterPublicApiPermissions.canUpdate(studyInstitutionId)) {
			throw new UpdateMoveStudyAccessDeniedException();
		}

		try {
			moveStudiesService.updateStatusAndResult(idMove, status, result);
			return true;
		} catch (Exception e) {
			throw new UpdateResultException(e.getMessage(), e);
		}
	}
}
