package ar.lamansys.sgh.publicapi.imagenetwork.application.check.exceptions;

import ar.lamansys.sgh.publicapi.infrastructure.input.rest.exceptions.PublicApiAccessDeniedException;

public class CheckStudyAccessDeniedException extends PublicApiAccessDeniedException {
	public CheckStudyAccessDeniedException() {
		super("ImageNetwork", "CheckStudyToken");
	}
}
