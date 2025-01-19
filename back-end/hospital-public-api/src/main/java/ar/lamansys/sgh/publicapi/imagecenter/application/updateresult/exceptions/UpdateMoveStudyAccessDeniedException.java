package ar.lamansys.sgh.publicapi.imagecenter.application.updateresult.exceptions;

import ar.lamansys.sgh.publicapi.generalexceptions.PublicApiAccessDeniedException;

public class UpdateMoveStudyAccessDeniedException extends PublicApiAccessDeniedException {
	public UpdateMoveStudyAccessDeniedException() {
		super("ImageCenter", "UpdateMoveStudy");
	}
}
