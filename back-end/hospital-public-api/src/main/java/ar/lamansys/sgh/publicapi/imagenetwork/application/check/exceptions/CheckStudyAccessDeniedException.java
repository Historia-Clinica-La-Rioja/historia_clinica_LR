package ar.lamansys.sgh.publicapi.imagenetwork.application.check.exceptions;

import ar.lamansys.sgh.publicapi.generalexceptions.PublicApiAccessDeniedException;

public class CheckStudyAccessDeniedException extends PublicApiAccessDeniedException {
	public CheckStudyAccessDeniedException() {
		super("ImageNetwork", "CheckStudyToken");
	}
}
