package ar.lamansys.sgh.publicapi.imagecenter.application.updatesize;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.publicapi.imagecenter.application.updateresult.exceptions.UpdateMoveStudyAccessDeniedException;
import ar.lamansys.sgh.publicapi.imagecenter.application.updatesize.exceptions.UpdateSizeException;
import ar.lamansys.sgh.publicapi.imagecenter.infrastructure.input.service.ImageCenterPublicApiPermissions;
import ar.lamansys.sgh.shared.infrastructure.input.service.imagenetwork.SharedLoadStudiesResultPort;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UpdateSize {
	public final ImageCenterPublicApiPermissions imageCenterPublicApiPermissions;
	public final SharedLoadStudiesResultPort moveStudiesService;

	public Boolean run(Integer idMove, Integer size, String imageId) throws UpdateSizeException {
		var studyInstitutionId = moveStudiesService.findInstitutionId(idMove)
				.orElseThrow(UpdateMoveStudyAccessDeniedException::new);

		if (!imageCenterPublicApiPermissions.canUpdate(studyInstitutionId)) {
			throw new UpdateMoveStudyAccessDeniedException();
		}

		try {
			moveStudiesService.updateSize(idMove, size, imageId);
			return true;
		} catch (Exception e) {
			throw new UpdateSizeException(e.getMessage(), e);
		}
	}
}
